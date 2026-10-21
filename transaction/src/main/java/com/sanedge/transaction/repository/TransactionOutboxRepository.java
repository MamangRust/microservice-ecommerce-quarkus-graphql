package com.sanedge.transaction.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.sanedge.transaction.entity.TransactionOutbox;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionOutboxRepository implements PanacheRepository<TransactionOutbox> {
    @ConfigProperty(name = "transaction.outbox.lease.seconds", defaultValue = "300")
    long leaseSeconds;

    @WithTransaction
    public Uni<Long> countPending() {
        return count("status = 'PENDING'");
    }

    /**
     * Stores a domain event in the transactional outbox.
     *
     * <p>When called from a business operation that already runs inside a
     * transaction the nested {@code @WithTransaction} joins that transaction,
     * so the event row and the business row are committed (or rolled back)
     * together. Called on its own it opens its own transaction.
     */
    @WithTransaction
    public Uni<TransactionOutbox> persistEvent(TransactionOutbox event) {
        return persist(event);
    }

    @WithTransaction
    public Uni<List<TransactionOutbox>> findDue(int limit) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Timestamp leaseCutoff = Timestamp.valueOf(LocalDateTime.now().minusSeconds(Math.max(1, leaseSeconds)));
        return find("status = 'PENDING' AND (nextAttemptAt IS NULL OR nextAttemptAt <= ?1) "
                + "AND (claimedAt IS NULL OR claimedAt < ?2) ORDER BY createdAt", now, leaseCutoff)
                .page(0, limit)
                .list();
    }

    @WithTransaction
    public Uni<TransactionOutbox> claim(TransactionOutbox event) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Timestamp leaseCutoff = Timestamp.valueOf(LocalDateTime.now().minusSeconds(Math.max(1, leaseSeconds)));
        String claimToken = UUID.randomUUID().toString();
        return update("claimedAt = ?1, claimToken = ?2, attempts = attempts + 1 "
                + "WHERE id = ?3 AND status = 'PENDING' "
                + "AND (claimedAt IS NULL OR claimedAt < ?4) "
                + "AND (nextAttemptAt IS NULL OR nextAttemptAt <= ?1)", now, claimToken, event.id, leaseCutoff)
                .chain(updated -> updated > 0 ? findById(event.id) : Uni.createFrom().nullItem());
    }

    @WithTransaction
    public Uni<Void> markSent(TransactionOutbox event) {
        return update("status = 'SENT', claimedAt = NULL, lastError = NULL "
                + "WHERE id = ?1 AND status = 'PENDING' AND claimToken = ?2", event.id, event.getClaimToken())
                .replaceWithVoid();
    }

    @WithTransaction
    public Uni<Void> markRetry(TransactionOutbox event, Throwable failure, long delaySeconds) {
        return update("status = 'PENDING', claimedAt = NULL, lastError = ?1, nextAttemptAt = ?2 "
                + "WHERE id = ?3 AND status = 'PENDING' AND claimToken = ?4",
                failure == null ? "unknown failure" : failure.getMessage(),
                Timestamp.valueOf(LocalDateTime.now().plusSeconds(delaySeconds)), event.id, event.getClaimToken())
                .replaceWithVoid();
    }
}