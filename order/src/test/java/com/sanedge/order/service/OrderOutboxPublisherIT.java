package com.sanedge.order.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.sanedge.common.observability.TracingMetrics;
import com.sanedge.common.test.KafkaResource;
import com.sanedge.common.test.KafkaTestUtils;
import com.sanedge.common.test.PostgreSqlResource;
import com.sanedge.order.entity.OrderOutbox;
import com.sanedge.order.repository.OrderOutboxRepository;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Integration tests for {@link OrderOutboxPublisher} against a REAL Kafka
 * broker (Testcontainers) and a real PostgreSQL: drain-to-SENT, exactly-once
 * under concurrent polls, lease re-processing after a crash window, no
 * republish of SENT events, and backoff-respecting polls.
 */
@QuarkusTest
@QuarkusTestResource(value = KafkaResource.class, restrictToAnnotatedClass = true)
@QuarkusTestResource(value = PostgreSqlResource.class, restrictToAnnotatedClass = true)
@TestProfile(OrderOutboxPublisherIT.NoSchedulerProfile.class)
@RunOnVertxContext
class OrderOutboxPublisherIT {

    public static class NoSchedulerProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of(
                    "quarkus.scheduler.enabled", "false",
                    "order.outbox.lease.seconds", "2");
        }
    }

    static final String TOPIC_PREFIX = "stats.ecommerce.order.event-it-";

    @Inject
    OrderOutboxPublisher publisher;

    @Inject
    OrderOutboxRepository repository;

    @InjectMock
    TracingMetrics tracingMetrics;

    @ConfigProperty(name = "kafka.bootstrap.servers")
    String bootstrap;

    private Uni<OrderOutbox> persistPending(String eventId, String topic) {
        OrderOutbox event = new OrderOutbox();
        event.setEventId(eventId);
        event.setTopic(topic);
        event.setEventKey("order-1");
        event.setPayload(new JsonObject()
                .put("event_id", eventId)
                .put("schema_version", 1)
                .put("event_type", topic)
                .put("occurred_at", LocalDateTime.now().toString())
                .put("payload", new JsonObject()
                        .put("order_id", 1)
                        .put("merchant_id", 100)
                        .put("status", "CREATED")
                        .put("total_amount", 1000.0))
                .encode());
        return Panache.withTransaction(() -> OrderOutbox.persist(event).replaceWith(event));
    }

    private Uni<OrderOutbox> findByEventId(String eventId) {
        return Panache.withSession(() -> repository.find("eventId", eventId).firstResult());
    }

    private Uni<Long> countRecordsOnTopic(String topic, String eventId) {
        return Uni.createFrom()
                .item(() -> KafkaTestUtils.countMatching(bootstrap, topic,
                        r -> eventId.equals(new JsonObject(r.value()).getString("event_id")),
                        Duration.ofSeconds(10)))
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    @Test
    void publishesPendingEventToKafkaAndMarksSent(UniAsserter asserter) {
        String topic = TOPIC_PREFIX + UUID.randomUUID();
        String eventId = "order-it-drain-" + UUID.randomUUID();

        asserter.execute(() -> persistPending(eventId, topic));
        asserter.execute(() -> publisher.poll());
        asserter.assertThat(() -> findByEventId(eventId), saved -> {
            assertThat(saved.getStatus()).isEqualTo("SENT");
            assertThat(saved.getLastError()).isNull();
        });
        asserter.assertThat(
                () -> Uni.createFrom()
                        .item(() -> KafkaTestUtils.awaitRecord(bootstrap, topic,
                                r -> eventId.equals(new JsonObject(r.value()).getString("event_id")),
                                Duration.ofSeconds(20)))
                        .runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
                record -> {
                    assertThat(record).isNotNull();
                    JsonObject payload = new JsonObject(record.value());
                    assertThat(payload.getString("event_type")).isEqualTo(topic);
                    assertThat(payload.getJsonObject("payload").getLong("order_id")).isEqualTo(1L);
                });
    }

    @Test
    void concurrentPollsPublishExactlyOnce(UniAsserter asserter) {
        String topic = TOPIC_PREFIX + UUID.randomUUID();
        String eventId = "order-it-exact-" + UUID.randomUUID();

        asserter.execute(() -> persistPending(eventId, topic));
        asserter.execute(() -> Uni.combine().all().unis(publisher.poll(), publisher.poll()).asTuple());
        asserter.assertThat(() -> findByEventId(eventId), saved -> {
            assertThat(saved.getStatus()).isEqualTo("SENT");
            assertThat(saved.getAttempts()).isEqualTo(1);
        });
        asserter.assertThat(() -> countRecordsOnTopic(topic, eventId),
                count -> assertThat(count).isEqualTo(1));
    }

    @Test
    void orphanedClaimIsReprocessedAfterLeaseExpiry(UniAsserter asserter) {
        String topic = TOPIC_PREFIX + UUID.randomUUID();
        String eventId = "order-it-lease-" + UUID.randomUUID();
        OrderOutbox[] ref = new OrderOutbox[1];

        asserter.execute(() -> persistPending(eventId, topic).invoke(e -> ref[0] = e));
        asserter.assertThat(() -> Panache.withTransaction(() -> repository.claim(ref[0])),
                claimed -> assertThat(claimed).isNotNull());
        asserter.execute(() -> Panache.withTransaction(() -> repository.update(
                "claimedAt = ?1 WHERE id = ?2",
                Timestamp.valueOf(LocalDateTime.now().minusSeconds(5)), ref[0].id)));
        asserter.execute(() -> publisher.poll());
        asserter.assertThat(() -> findByEventId(eventId), saved -> {
            assertThat(saved.getStatus()).isEqualTo("SENT");
            assertThat(saved.getAttempts()).isEqualTo(2);
        });
        asserter.assertThat(() -> countRecordsOnTopic(topic, eventId),
                count -> assertThat(count).isEqualTo(1));
    }

    @Test
    void sentEventIsNotRepublishedByLaterPolls(UniAsserter asserter) {
        String topic = TOPIC_PREFIX + UUID.randomUUID();
        String eventId = "order-it-sent-" + UUID.randomUUID();

        asserter.execute(() -> persistPending(eventId, topic));
        asserter.execute(() -> publisher.poll());
        asserter.execute(() -> publisher.poll());
        asserter.assertThat(() -> findByEventId(eventId), saved -> {
            assertThat(saved.getStatus()).isEqualTo("SENT");
            assertThat(saved.getAttempts()).isEqualTo(1);
        });
        asserter.assertThat(() -> countRecordsOnTopic(topic, eventId),
                count -> assertThat(count).isEqualTo(1));
    }

    @Test
    void nonDueEventWithFutureBackoffIsNotPolled(UniAsserter asserter) {
        String topic = TOPIC_PREFIX + UUID.randomUUID();
        String eventId = "order-it-backoff-" + UUID.randomUUID();
        OrderOutbox[] ref = new OrderOutbox[1];

        asserter.execute(() -> persistPending(eventId, topic).invoke(e -> ref[0] = e));
        asserter.execute(() -> Panache.withTransaction(() -> repository.update(
                "nextAttemptAt = ?1 WHERE id = ?2",
                Timestamp.valueOf(LocalDateTime.now().plusMinutes(30)), ref[0].id)));
        asserter.execute(() -> publisher.poll());
        asserter.assertThat(() -> findByEventId(eventId), saved -> {
            assertThat(saved.getStatus()).isEqualTo("PENDING");
            assertThat(saved.getAttempts()).isZero();
        });
        asserter.assertThat(() -> countRecordsOnTopic(topic, eventId),
                count -> assertThat(count).isZero());
    }
}
