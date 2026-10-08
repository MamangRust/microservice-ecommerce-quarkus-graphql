package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.OrderItemDto;
import com.sanedge.gateway.service.OrderItemService;
import com.sanedge.gateway.telemetry.TelemetryHelper;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderItemServiceImpl implements OrderItemService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("order_item")
    pb.order_item.MutinyOrderItemQueryServiceGrpc.MutinyOrderItemQueryServiceStub orderItemQueryService;

    @Override
    public Uni<OrderItemDto.FindAllOrderItemResponse> listOrderItems(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("orderItem.listOrderItems",
                () -> orderItemQueryService.findAll(pb.order_item.OrderItemQuery.FindAllOrderItemRequest.newBuilder()
                        .setPage(page)
                        .setPageSize(size)
                        .setSearch(search == null ? "" : search)
                        .build())
                        .map(OrderItemDto.FindAllOrderItemResponse::from));
    }

    @Override
    public Uni<OrderItemDto.FindAllOrderItemDeleteAtResponse> getActiveOrderItems(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("orderItem.getActiveOrderItems",
                () -> orderItemQueryService.findByActive(pb.order_item.OrderItemQuery.FindAllOrderItemRequest.newBuilder()
                        .setPage(page)
                        .setPageSize(size)
                        .setSearch(search == null ? "" : search)
                        .build())
                        .map(OrderItemDto.FindAllOrderItemDeleteAtResponse::from));
    }

    @Override
    public Uni<OrderItemDto.FindAllOrderItemDeleteAtResponse> getTrashedOrderItems(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("orderItem.getTrashedOrderItems",
                () -> orderItemQueryService.findByTrashed(pb.order_item.OrderItemQuery.FindAllOrderItemRequest.newBuilder()
                        .setPage(page)
                        .setPageSize(size)
                        .setSearch(search == null ? "" : search)
                        .build())
                        .map(OrderItemDto.FindAllOrderItemDeleteAtResponse::from));
    }

    @Override
    public Uni<OrderItemDto.FindByOrderResponse> getOrderItemsByOrder(int orderId) {
        return telemetryHelper.traceAndMetric("orderItem.getOrderItemsByOrder",
                () -> orderItemQueryService.findOrderItemByOrder(
                        pb.order_item.OrderItemCommon.FindByIdOrderItemRequest.newBuilder()
                                .setId(orderId)
                                .build())
                        .map(OrderItemDto.FindByOrderResponse::from));
    }
}
