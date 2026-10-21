package com.sanedge.order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.sanedge.common.event.EventTopics;
import com.sanedge.common.test.PostgreSqlResource;
import com.sanedge.common.test.RedisResource;
import com.sanedge.order.entity.Order;
import com.sanedge.order.entity.OrderOutbox;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;

/**
 * Proves the transactional-outbox guarantee of the order module: the
 * {@code order_outbox} row and the {@code orders} row are written in the very
 * same database transaction, so an event exists if and only if the order
 * exists.
 */
@QuarkusTest
@QuarkusTestResource(value = PostgreSqlResource.class, restrictToAnnotatedClass = true)
@QuarkusTestResource(value = RedisResource.class, restrictToAnnotatedClass = true)
@RunOnVertxContext
class OrderOutboxAtomicityIT {

    private static final int MERCHANT_OK = 4242;
    private static final int MERCHANT_ROLLBACK = 4343;

    @Inject
    OrderCommandRepository orderCommandRepository;

    @Inject
    OrderOutboxRepository orderOutboxRepository;

    private Uni<Order> newOrder(int merchantId) {
        Order order = new Order();
        order.setMerchantId(merchantId);
        order.setUserId(merchantId);
        order.setTotalPrice(0);
        return orderCommandRepository.persistNew(order);
    }

    private OrderOutbox newOutboxEvent(String eventKey) {
        String eventId = UUID.randomUUID().toString();
        OrderOutbox outbox = new OrderOutbox();
        outbox.setEventId(eventId);
        outbox.setTopic(EventTopics.ORDER);
        outbox.setEventKey(eventKey);
        outbox.setPayload(new JsonObject().put("event_id", eventId).encode());
        return outbox;
    }

    private Uni<Long> countOrders(int merchantId) {
        return Panache.withSession(() -> orderCommandRepository.count("merchantId", merchantId));
    }

    private Uni<Long> countEvents(String eventKey) {
        return Panache.withSession(() -> orderOutboxRepository.count("eventKey", eventKey));
    }

    @Test
    void orderAndOutboxEventAreCommittedTogether(UniAsserter asserter) {
        asserter.assertThat(
                () -> Panache.withTransaction(() -> newOrder(MERCHANT_OK)
                        .chain(order -> orderOutboxRepository
                                .persistEvent(newOutboxEvent(String.valueOf(MERCHANT_OK)))
                                .replaceWith(order))),
                order -> assertThat(order).isNotNull());

        asserter.assertThat(() -> countOrders(MERCHANT_OK), count -> assertThat(count).isEqualTo(1L));
        asserter.assertThat(() -> countEvents(String.valueOf(MERCHANT_OK)),
                count -> assertThat(count).isEqualTo(1L));
    }

    @Test
    void aFailingOutboxWriteRollsTheOrderBack(UniAsserter asserter) {
        asserter.assertFailedWith(
                () -> Panache.withTransaction(() -> newOrder(MERCHANT_ROLLBACK)
                        .chain(order -> {
                            // event_id is NOT NULL — the insert fails inside the
                            // very same transaction that inserted the order.
                            OrderOutbox outbox = newOutboxEvent(String.valueOf(MERCHANT_ROLLBACK));
                            outbox.setEventId(null);
                            return orderOutboxRepository.persistEvent(outbox).replaceWith(order);
                        })),
                error -> assertThat(error).isNotNull());

        // Neither the order nor the event survived the failed transaction.
        asserter.assertThat(() -> countOrders(MERCHANT_ROLLBACK), count -> assertThat(count).isZero());
        asserter.assertThat(() -> countEvents(String.valueOf(MERCHANT_ROLLBACK)),
                count -> assertThat(count).isZero());
    }
}
