package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.OrderDto.ApiResponseOrderMonthly;
import com.sanedge.gateway.dto.OrderDto.ApiResponseOrderYearly;
import com.sanedge.gateway.dto.OrderDto.CreateOrderRequest;
import com.sanedge.gateway.dto.OrderDto.CreateOrderResponse;
import com.sanedge.gateway.dto.OrderDto.FindAllOrderResponse;
import com.sanedge.gateway.dto.OrderDto.FindByIdOrderResponse;
import com.sanedge.gateway.dto.OrderDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.OrderDto.UpdateOrderRequest;
import com.sanedge.gateway.dto.OrderDto.UpdateOrderResponse;
import com.sanedge.gateway.service.OrderService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class OrderResource {

    @Inject
    OrderService orderService;

    @Query("listOrders")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllOrderResponse> listOrders(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return orderService.listOrders(page, size, search);
    }

    @Query("listActiveOrders")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllOrderResponse> listActiveOrders(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return orderService.listActiveOrders(page, size, search);
    }

    @Query("listTrashedOrders")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindAllOrderResponse> listTrashedOrders(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return orderService.listTrashedOrders(page, size, search);
    }

    @Query("getOrder")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindByIdOrderResponse> getOrder(@Name("id") int id) {
        return orderService.getOrder(id);
    }

    @Mutation("createOrder")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<CreateOrderResponse> createOrder(@Name("body") CreateOrderRequest body) {
        return orderService.createOrder(body);
    }

    @Mutation("updateOrder")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<UpdateOrderResponse> updateOrder(@Name("id") int id, @Name("body") UpdateOrderRequest body) {
        return orderService.updateOrder(id, body);
    }

    @Mutation("deleteOrder")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdOrderResponse> deleteOrder(@Name("id") int id) {
        return orderService.deleteOrder(id);
    }

    @Mutation("trashedOrder")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdOrderResponse> trashedOrder(@Name("id") int id) {
        return orderService.deleteOrder(id);
    }

    @Mutation("restoreOrder")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdOrderResponse> restoreOrder(@Name("id") int id) {
        return orderService.restoreOrder(id);
    }

    @Mutation("deleteOrderPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteOrderPermanent(@Name("id") int id) {
        return orderService.deleteOrderPermanent(id);
    }

    @Mutation("restoreAllOrders")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> restoreAllOrders() {
        return orderService.restoreAllOrders();
    }

    @Mutation("deleteAllOrdersPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteAllOrdersPermanent() {
        return orderService.deleteAllOrdersPermanent();
    }

    // STATS
    @Query("getMonthlyRevenueStats")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<ApiResponseOrderMonthly> getMonthlyRevenueStats(@Name("year") int year, @Name("month") int month) {
        return orderService.getMonthlyRevenueStats(year, month);
    }

    @Query("getYearlyRevenueStats")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<ApiResponseOrderYearly> getYearlyRevenueStats(@Name("year") int year) {
        return orderService.getYearlyRevenueStats(year);
    }
}
