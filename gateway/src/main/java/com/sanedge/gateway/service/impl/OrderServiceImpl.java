package com.sanedge.gateway.service.impl;

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
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("order")
    pb.order.MutinyOrderQueryServiceGrpc.MutinyOrderQueryServiceStub orderQueryService;

    @GrpcClient("order")
    pb.order.MutinyOrderCommandServiceGrpc.MutinyOrderCommandServiceStub orderCommandService;

    @GrpcClient("statsreader")
    pb.order.stats.MutinyOrderRevenueServiceGrpc.MutinyOrderRevenueServiceStub orderRevenueService;

    @Override
    public Uni<FindAllOrderResponse> listOrders(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("order.listOrders", () -> orderQueryService.findAll(pb.order.OrderQuery.FindAllOrderRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllOrderResponse::from));
    }

    @Override
    public Uni<FindAllOrderResponse> listActiveOrders(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("order.listActiveOrders", () -> orderQueryService.findByActive(pb.order.OrderQuery.FindAllOrderRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllOrderResponse::from));
    }

    @Override
    public Uni<FindAllOrderResponse> listTrashedOrders(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("order.listTrashedOrders", () -> orderQueryService.findByTrashed(pb.order.OrderQuery.FindAllOrderRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllOrderResponse::from));
    }

    @Override
    public Uni<FindByIdOrderResponse> getOrder(int id) {
        return telemetryHelper.traceAndMetric("order.getOrder", () -> orderQueryService.findById(pb.order.OrderCommon.FindByIdOrderRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdOrderResponse::from));
    }

    @Override
    public Uni<CreateOrderResponse> createOrder(CreateOrderRequest body) {
        return telemetryHelper.traceAndMetric("order.createOrder", () -> {
            List<pb.order.OrderCommand.CreateOrderItemRequest> protoItems = new ArrayList<>();
            if (body.items() != null) {
                for (var item : body.items()) {
                    protoItems.add(pb.order.OrderCommand.CreateOrderItemRequest.newBuilder()
                            .setProductId(item.productId())
                            .setQuantity(item.quantity())
                            .setPrice(item.price())
                            .build());
                }
            }

            pb.order.OrderCommand.CreateOrderRequest.Builder builder = pb.order.OrderCommand.CreateOrderRequest.newBuilder()
                    .setMerchantId(body.merchantId())
                    .setUserId(body.userId())
                    .setTotalPrice(body.totalPrice())
                    .addAllItems(protoItems);

            if (body.shipping() != null) {
                builder.setShipping(pb.shipping_address.ShippingAddressCommand.CreateShippingAddressRequest.newBuilder()
                        .setAlamat(body.shipping().alamat() == null ? "" : body.shipping().alamat())
                        .setProvinsi(body.shipping().provinsi() == null ? "" : body.shipping().provinsi())
                        .setKota(body.shipping().kota() == null ? "" : body.shipping().kota())
                        .setCourier(body.shipping().courier() == null ? "" : body.shipping().courier())
                        .setShippingMethod(body.shipping().shippingMethod() == null ? "" : body.shipping().shippingMethod())
                        .setShippingCost(body.shipping().shippingCost())
                        .setNegara(body.shipping().negara() == null ? "" : body.shipping().negara())
                        .build());
            }

            return orderCommandService.create(builder.build()).map(CreateOrderResponse::from);
        });
    }

    @Override
    public Uni<UpdateOrderResponse> updateOrder(int id, UpdateOrderRequest body) {
        return telemetryHelper.traceAndMetric("order.updateOrder", () -> {
            List<pb.order.OrderCommand.UpdateOrderItemRequest> protoItems = new ArrayList<>();
            if (body.items() != null) {
                for (var item : body.items()) {
                    protoItems.add(pb.order.OrderCommand.UpdateOrderItemRequest.newBuilder()
                            .setOrderItemId(item.orderItemId())
                            .setProductId(item.productId())
                            .setQuantity(item.quantity())
                            .setPrice(item.price())
                            .build());
                }
            }

            pb.order.OrderCommand.UpdateOrderRequest.Builder builder = pb.order.OrderCommand.UpdateOrderRequest.newBuilder()
                    .setOrderId(id)
                    .setUserId(body.userId())
                    .setTotalPrice(body.totalPrice())
                    .addAllItems(protoItems);

            if (body.shipping() != null) {
                builder.setShipping(pb.shipping_address.ShippingAddressCommand.UpdateShippingAddressRequest.newBuilder()
                        .setShippingId(body.shipping().shippingId())
                        .setOrderId(id)
                        .setAlamat(body.shipping().alamat() == null ? "" : body.shipping().alamat())
                        .setProvinsi(body.shipping().provinsi() == null ? "" : body.shipping().provinsi())
                        .setKota(body.shipping().kota() == null ? "" : body.shipping().kota())
                        .setCourier(body.shipping().courier() == null ? "" : body.shipping().courier())
                        .setShippingMethod(body.shipping().shippingMethod() == null ? "" : body.shipping().shippingMethod())
                        .setShippingCost(body.shipping().shippingCost())
                        .setNegara(body.shipping().negara() == null ? "" : body.shipping().negara())
                        .build());
            }

            return orderCommandService.update(builder.build()).map(UpdateOrderResponse::from);
        });
    }

    @Override
    public Uni<FindByIdOrderResponse> deleteOrder(int id) {
        return telemetryHelper.traceAndMetric("order.deleteOrder", () -> orderCommandService.trashedOrder(pb.order.OrderCommon.FindByIdOrderRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdOrderResponse::from));
    }

    @Override
    public Uni<FindByIdOrderResponse> restoreOrder(int id) {
        return telemetryHelper.traceAndMetric("order.restoreOrder", () -> orderCommandService.restoreOrder(pb.order.OrderCommon.FindByIdOrderRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdOrderResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteOrderPermanent(int id) {
        return telemetryHelper.traceAndMetric("order.deleteOrderPermanent", () -> orderCommandService.deleteOrderPermanent(pb.order.OrderCommon.FindByIdOrderRequest.newBuilder()
                .setId(id)
                .build())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllOrders() {
        return telemetryHelper.traceAndMetric("order.restoreAllOrders", () -> orderCommandService.restoreAllOrder(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllOrdersPermanent() {
        return telemetryHelper.traceAndMetric("order.deleteAllOrdersPermanent", () -> orderCommandService.deleteAllOrderPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<ApiResponseOrderMonthly> getMonthlyRevenueStats(int year, int month) {
        return telemetryHelper.traceAndMetric("order.getMonthlyRevenueStats", () -> orderRevenueService.findMonthlyRevenue(pb.order.stats.OrderRevenue.FindYearOrder.newBuilder()
                .setYear(year)
                .setMonth(month)
                .build())
                .map(ApiResponseOrderMonthly::from));
    }

    @Override
    public Uni<ApiResponseOrderYearly> getYearlyRevenueStats(int year) {
        return telemetryHelper.traceAndMetric("order.getYearlyRevenueStats", () -> orderRevenueService.findYearlyRevenue(pb.order.stats.OrderRevenue.FindYearOrder.newBuilder()
                .setYear(year)
                .build())
                .map(ApiResponseOrderYearly::from));
    }
}
