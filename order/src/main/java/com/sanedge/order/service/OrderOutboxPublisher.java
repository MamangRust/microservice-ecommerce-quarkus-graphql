package com.sanedge.order.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.sanedge.common.observability.TracingMetrics;
import com.sanedge.order.entity.OrderOutbox;
import com.sanedge.order.repository.OrderOutboxRepository;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transactional outbox relay for the order module. Polls the PENDING rows of
 * the {@code order_outbox} table, claims them with a lease (so that the
 * replicas of this service never relay the same row twice) and pushes them to
 * Kafka. A failed publish is retried with an exponential backoff; the row
 * stays PENDING until the lease expires.
 */
@ApplicationScoped
public class OrderOutboxPublisher {
    private static final Logger log = LoggerFactory.getLogger(OrderOutboxPublisher.class);

    @Inject OrderOutboxRepository repository;
    @Inject KafkaService kafkaService;
    @Inject TracingMetrics tracingMetrics;

    @ConfigProperty(name = "order.outbox.batch-size", defaultValue = "50")
    int batchSize;

    private final AtomicBoolean polling = new AtomicBoolean();
    private final AtomicLong backlog = new AtomicLong(0);

    void onStart(@Observes StartupEvent event) {
        if (tracingMetrics != null) {
            // The gauge supplier runs on the OpenTelemetry metric thread, which has
            // no duplicated Vert.x context - reading the reactive backlog from there
            // fails with "No current Vertx context". Read the cached counter that the
            // scheduled poller refreshes instead.
            tracingMetrics.registerOutboxBacklogGauge("order", backlog::get);
        }
    }

    @Scheduled(every = "1s", delay = 5, delayUnit = TimeUnit.SECONDS)
    Uni<Void> poll() {
        if (!polling.compareAndSet(false, true)) return Uni.createFrom().voidItem();
        return publishDue()
                .onItem().transformToUni(v -> repository.countPending())
                .invoke(count -> { backlog.set(count); polling.set(false); })
                .replaceWithVoid()
                .onFailure().invoke(err -> { log.error("Order outbox poll failed", err); polling.set(false); });
    }

    private Uni<Void> publishDue() {
        return repository.findDue(Math.max(1, batchSize)).chain(this::publishSequentially);
    }

    private Uni<Void> publishSequentially(List<OrderOutbox> events) {
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (OrderOutbox event : events) {
            chain = chain.chain(() -> repository.claim(event)
                    .chain(claimed -> {
                        if (claimed == null) return Uni.createFrom().voidItem();
                        return kafkaService.sendExistingEvent(claimed.getTopic(), claimed.getEventKey(),
                                new JsonObject(claimed.getPayload()))
                                .chain(() -> repository.markSent(claimed))
                                .invoke(() -> {
                                    if (tracingMetrics != null) {
                                        tracingMetrics.recordOutboxPublished("order");
                                    }
                                })
                                .onFailure().recoverWithUni(error -> repository.markRetry(claimed, error,
                                        retryDelaySeconds(claimed.getAttempts())));
                    }));
        }
        return chain;
    }

    private long retryDelaySeconds(int attempts) {
        return Math.min(300, 1L << Math.min(8, Math.max(0, attempts - 1)));
    }
}
