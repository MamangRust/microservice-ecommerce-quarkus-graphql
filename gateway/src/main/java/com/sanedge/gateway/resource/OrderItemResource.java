package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.OrderItemDto;
import com.sanedge.gateway.service.OrderItemService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class OrderItemResource {

    @Inject
    OrderItemService orderItemService;

    @Query("listOrderItems")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<OrderItemDto.FindAllOrderItemResponse> listOrderItems(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return orderItemService.listOrderItems(page, size, search);
    }

    @Query("activeOrderItems")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<OrderItemDto.FindAllOrderItemDeleteAtResponse> getActiveOrderItems(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return orderItemService.getActiveOrderItems(page, size, search);
    }

    @Query("trashedOrderItems")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<OrderItemDto.FindAllOrderItemDeleteAtResponse> getTrashedOrderItems(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return orderItemService.getTrashedOrderItems(page, size, search);
    }

    @Query("orderItemsByOrder")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<OrderItemDto.FindByOrderResponse> getOrderItemsByOrder(@Name("orderId") int orderId) {
        return orderItemService.getOrderItemsByOrder(orderId);
    }
}
