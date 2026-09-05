package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.CartDto.CreateCartRequest;
import com.sanedge.gateway.dto.CartDto.CreateCartResponse;
import com.sanedge.gateway.dto.CartDto.FindAllCartResponse;
import com.sanedge.gateway.dto.CartDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.service.CartService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CartServiceImpl implements CartService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("cart")
    pb.cart.MutinyCartQueryServiceGrpc.MutinyCartQueryServiceStub cartQueryService;

    @GrpcClient("cart")
    pb.cart.MutinyCartCommandServiceGrpc.MutinyCartCommandServiceStub cartCommandService;

    @Override
    public Uni<FindAllCartResponse> listCarts(int userId, int page, int size, String search) {
        return telemetryHelper.traceAndMetric("cart.listCarts",
                () -> cartQueryService.findAll(pb.cart.CartQuery.FindAllCartRequest.newBuilder()
                        .setUserId(userId)
                        .setPage(page)
                        .setPageSize(size)
                        .setSearch(search == null ? "" : search)
                        .build())
                        .map(FindAllCartResponse::from));
    }

    @Override
    public Uni<CreateCartResponse> createCart(CreateCartRequest body) {
        return telemetryHelper.traceAndMetric("cart.createCart",
                () -> cartCommandService.create(pb.cart.CartCommand.CreateCartRequest.newBuilder()
                        .setQuantity(body.quantity())
                        .setProductId(body.productId())
                        .setUserId(body.userId())
                        .build())
                        .map(CreateCartResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteCart(int cartId, int userId) {
        return telemetryHelper.traceAndMetric("cart.deleteCart",
                () -> cartCommandService.delete(pb.cart.CartCommand.DeleteCartRequest.newBuilder()
                        .setCartId(cartId)
                        .setUserId(userId)
                        .build())
                        .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllCarts(int userId, List<Integer> cartIds) {
        return telemetryHelper.traceAndMetric("cart.deleteAllCarts", () -> {
            pb.cart.CartCommand.DeleteAllCartRequest.Builder builder = pb.cart.CartCommand.DeleteAllCartRequest
                    .newBuilder()
                    .setUserId(userId);
            if (cartIds != null) {
                builder.addAllCartIds(cartIds);
            }
            return cartCommandService.deleteAll(builder.build())
                    .map(SimpleStatusMessageResponse::from);
        });
    }
}
