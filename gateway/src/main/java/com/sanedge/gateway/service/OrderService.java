package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.OrderDto.ApiResponseOrderMonthly;
import com.sanedge.gateway.dto.OrderDto.ApiResponseOrderYearly;
import com.sanedge.gateway.dto.OrderDto.CreateOrderRequest;
import com.sanedge.gateway.dto.OrderDto.CreateOrderResponse;
import com.sanedge.gateway.dto.OrderDto.FindAllOrderResponse;
import com.sanedge.gateway.dto.OrderDto.FindByIdOrderResponse;
import com.sanedge.gateway.dto.OrderDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.OrderDto.UpdateOrderRequest;
import com.sanedge.gateway.dto.OrderDto.UpdateOrderResponse;
import io.smallrye.mutiny.Uni;

public interface OrderService {
    Uni<FindAllOrderResponse> listOrders(int page, int size, String search);
    Uni<FindAllOrderResponse> listActiveOrders(int page, int size, String search);
    Uni<FindAllOrderResponse> listTrashedOrders(int page, int size, String search);
    Uni<FindByIdOrderResponse> getOrder(int id);
    Uni<CreateOrderResponse> createOrder(CreateOrderRequest body);
    Uni<UpdateOrderResponse> updateOrder(int id, UpdateOrderRequest body);
    Uni<FindByIdOrderResponse> deleteOrder(int id);
    Uni<FindByIdOrderResponse> restoreOrder(int id);
    Uni<SimpleStatusMessageResponse> deleteOrderPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllOrders();
    Uni<SimpleStatusMessageResponse> deleteAllOrdersPermanent();
    Uni<ApiResponseOrderMonthly> getMonthlyRevenueStats(int year, int month);
    Uni<ApiResponseOrderYearly> getYearlyRevenueStats(int year);
}
