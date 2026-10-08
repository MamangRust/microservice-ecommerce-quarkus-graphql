package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.OrderItemDto;

import io.smallrye.mutiny.Uni;

public interface OrderItemService {
    Uni<OrderItemDto.FindAllOrderItemResponse> listOrderItems(int page, int size, String search);

    Uni<OrderItemDto.FindAllOrderItemDeleteAtResponse> getActiveOrderItems(int page, int size, String search);

    Uni<OrderItemDto.FindAllOrderItemDeleteAtResponse> getTrashedOrderItems(int page, int size, String search);

    Uni<OrderItemDto.FindByOrderResponse> getOrderItemsByOrder(int orderId);
}
