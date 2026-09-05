package com.sanedge.gateway.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sanedge.gateway.dto.TransactionDto;
import com.sanedge.gateway.telemetry.TelemetryHelper;

import io.smallrye.mutiny.Uni;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TelemetryHelper telemetryHelper;
    @Mock
    private pb.transaction.MutinyTransactionQueryServiceGrpc.MutinyTransactionQueryServiceStub transactionQueryService;
    @Mock
    private pb.transaction.MutinyTransactionCommandServiceGrpc.MutinyTransactionCommandServiceStub transactionCommandService;

    private TransactionServiceImpl service;

    private void inject(String name, Object value) throws Exception {
        Field f = TransactionServiceImpl.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(service, value);
    }

    private void injectNull(String name) throws Exception {
        inject(name, null);
    }

    @BeforeEach
    void setUp() throws Exception {
        lenient().when(telemetryHelper.traceAndMetric(anyString(), any(Supplier.class)))
                .thenAnswer(invocation -> {
                    @SuppressWarnings("unchecked")
                    Supplier<Uni<?>> supplier = invocation.getArgument(1);
                    return supplier.get();
                });
        service = new TransactionServiceImpl();
        inject("telemetryHelper", telemetryHelper);
        inject("transactionQueryService", transactionQueryService);
        inject("transactionCommandService", transactionCommandService);

        injectNull("transactionAmountService");
        injectNull("transactionMethodService");
    }

    @Test
    void findById_PropagatesTransactionResponse() {
        pb.transaction.TransactionCommon.ApiResponseTransaction proto = pb.transaction.TransactionCommon.ApiResponseTransaction.newBuilder()
                .setStatus("success").setMessage("ok").build();
        lenient().when(transactionQueryService.findById(any(pb.transaction.TransactionCommon.FindByIdTransactionRequest.class)))
                .thenAnswer(inv -> Uni.createFrom().item(proto));

        var result = service.getTransaction(1).await().indefinitely();
        assertThat(result.status()).isEqualTo("success");
    }

    @Test
    void findByOrderId_PropagatesTransactionResponse() {
        pb.transaction.TransactionCommon.ApiResponseTransaction proto = pb.transaction.TransactionCommon.ApiResponseTransaction.newBuilder()
                .setStatus("success").setMessage("ok").build();
        lenient().when(transactionQueryService.findByOrderId(any(pb.transaction.TransactionQuery.FindByOrderIdTransactionRequest.class)))
                .thenAnswer(inv -> Uni.createFrom().item(proto));

        var result = service.getTransactionByOrder(1).await().indefinitely();
        assertThat(result.message()).isEqualTo("ok");
    }

    @Test
    void create_PropagatesTransactionResponse() {
        pb.transaction.TransactionCommon.ApiResponseTransaction proto = pb.transaction.TransactionCommon.ApiResponseTransaction.newBuilder()
                .setStatus("success").setMessage("created").build();
        TransactionDto.CreateTransactionRequest req = new TransactionDto.CreateTransactionRequest(1, 1, "card", 1000, "pending");
        lenient().when(transactionCommandService.create(any(pb.transaction.TransactionCommand.CreateTransactionRequest.class)))
                .thenAnswer(inv -> Uni.createFrom().item(proto));

        var result = service.createTransaction(req).await().indefinitely();
        assertThat(result.message()).isEqualTo("created");
    }

    @Test
    void delete_TrashStub_PropagatesTransactionDeleteAt() {
        pb.transaction.TransactionCommon.ApiResponseTransactionDeleteAt proto = pb.transaction.TransactionCommon.ApiResponseTransactionDeleteAt.newBuilder()
                .setStatus("success").setMessage("trashed").build();
        lenient().when(transactionCommandService.trashedTransaction(any(pb.transaction.TransactionCommon.FindByIdTransactionRequest.class)))
                .thenAnswer(inv -> Uni.createFrom().item(proto));

        var result = service.deleteTransaction(1).await().indefinitely();
        assertThat(result.message()).isEqualTo("trashed");
    }
    @Test
    void restore_RestoreStub_Propagates() {
        pb.transaction.TransactionCommon.ApiResponseTransactionDeleteAt proto = pb.transaction.TransactionCommon.ApiResponseTransactionDeleteAt.newBuilder()
                .setStatus("success").setMessage("restored").build();
        lenient().when(transactionCommandService.restoreTransaction(any(pb.transaction.TransactionCommon.FindByIdTransactionRequest.class)))
                .thenAnswer(inv -> Uni.createFrom().item(proto));

        var result = service.restoreTransaction(1).await().indefinitely();
        assertThat(result.message()).isEqualTo("restored");
    }

}
