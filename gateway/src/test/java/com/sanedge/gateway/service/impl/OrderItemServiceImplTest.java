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

import com.sanedge.gateway.telemetry.TelemetryHelper;

import io.smallrye.mutiny.Uni;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private TelemetryHelper telemetryHelper;
    @Mock
    private pb.order_item.MutinyOrderItemQueryServiceGrpc.MutinyOrderItemQueryServiceStub orderItemQueryService;

    private OrderItemServiceImpl service;

    private void inject(String name, Object value) throws Exception {
        Field f = OrderItemServiceImpl.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(service, value);
    }

    @BeforeEach
    void setUp() throws Exception {
        lenient().when(telemetryHelper.traceAndMetric(anyString(), any(Supplier.class)))
                .thenAnswer(invocation -> {
                    @SuppressWarnings("unchecked")
                    Supplier<Uni<?>> supplier = invocation.getArgument(1);
                    return supplier.get();
                });
        service = new OrderItemServiceImpl();
        inject("telemetryHelper", telemetryHelper);
        inject("orderItemQueryService", orderItemQueryService);
    }

    @Test
    void listOrderItems_PropagatesPagination() {
        pb.order_item.OrderItemCommon.ApiResponsePaginationOrderItem proto = pb.order_item.OrderItemCommon.ApiResponsePaginationOrderItem
                .newBuilder()
                .setStatus("success").setMessage("ok")
                .setPagination(pb.Api.PaginationMeta.newBuilder()
                        .setCurrentPage(1).setPageSize(10).setTotalPages(1).setTotalRecords(3).build())
                .build();
        lenient().when(orderItemQueryService.findAll(any(pb.order_item.OrderItemQuery.FindAllOrderItemRequest.class)))
                .thenAnswer(inv -> Uni.createFrom().item(proto));

        var result = service.listOrderItems(1, 10, "").await().indefinitely();
        assertThat(result.status()).isEqualTo("success");
        assertThat(result.paginationMeta()).isNotNull();
        assertThat(result.paginationMeta().totalRecords()).isEqualTo(3);
    }

    @Test
    void getOrderItemsByOrder_PropagatesList() {
        pb.order_item.OrderItemCommon.ApiResponsesOrderItem proto = pb.order_item.OrderItemCommon.ApiResponsesOrderItem
                .newBuilder()
                .setStatus("success").setMessage("ok").build();
        lenient().when(orderItemQueryService
                .findOrderItemByOrder(any(pb.order_item.OrderItemCommon.FindByIdOrderItemRequest.class)))
                .thenAnswer(inv -> Uni.createFrom().item(proto));

        var result = service.getOrderItemsByOrder(1).await().indefinitely();
        assertThat(result.status()).isEqualTo("success");
        assertThat(result.data()).isEmpty();
    }
}
