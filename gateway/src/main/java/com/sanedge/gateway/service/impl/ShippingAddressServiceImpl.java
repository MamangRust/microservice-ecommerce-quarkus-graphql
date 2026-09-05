package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.ShippingAddressDto.CreateShippingAddressRequest;
import com.sanedge.gateway.dto.ShippingAddressDto.CreateShippingAddressResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.FindAllShippingResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.FindByIdShippingResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.UpdateShippingAddressRequest;
import com.sanedge.gateway.dto.ShippingAddressDto.UpdateShippingAddressResponse;
import com.sanedge.gateway.service.ShippingAddressService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ShippingAddressServiceImpl implements ShippingAddressService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("shipping_address")
    pb.shipping_address.MutinyShippingQueryServiceGrpc.MutinyShippingQueryServiceStub shippingQueryService;

    @GrpcClient("shipping_address")
    pb.shipping_address.MutinyShippingCommandServiceGrpc.MutinyShippingCommandServiceStub shippingCommandService;

    @Override
    public Uni<FindAllShippingResponse> listShippingAddresses(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("shippingAddress.listShippingAddresses", () -> shippingQueryService.findAll(pb.shipping_address.ShippingAddressQuery.FindAllShippingRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllShippingResponse::from));
    }

    @Override
    public Uni<FindByIdShippingResponse> getShippingAddressByOrder(int orderId) {
        return telemetryHelper.traceAndMetric("shippingAddress.getShippingAddressByOrder", () -> shippingQueryService.findByOrder(pb.shipping_address.ShippingAddressCommon.FindByIdShippingRequest.newBuilder()
                .setId(orderId)
                .build())
                .map(FindByIdShippingResponse::from));
    }

    @Override
    public Uni<FindAllShippingResponse> listActiveShippingAddresses(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("shippingAddress.listActiveShippingAddresses", () -> shippingQueryService.findByActive(pb.shipping_address.ShippingAddressQuery.FindAllShippingRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllShippingResponse::from));
    }

    @Override
    public Uni<FindAllShippingResponse> listTrashedShippingAddresses(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("shippingAddress.listTrashedShippingAddresses", () -> shippingQueryService.findByTrashed(pb.shipping_address.ShippingAddressQuery.FindAllShippingRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllShippingResponse::from));
    }

    @Override
    public Uni<FindByIdShippingResponse> getShippingAddress(int id) {
        return telemetryHelper.traceAndMetric("shippingAddress.getShippingAddress", () -> shippingQueryService.findById(pb.shipping_address.ShippingAddressCommon.FindByIdShippingRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdShippingResponse::from));
    }

    @Override
    public Uni<CreateShippingAddressResponse> createShippingAddress(CreateShippingAddressRequest body) {
        return telemetryHelper.traceAndMetric("shippingAddress.createShippingAddress", () -> shippingCommandService.createShipping(pb.shipping_address.ShippingAddressCommand.CreateShippingAddressRequest.newBuilder()
                .setOrderId(body.orderId())
                .setAlamat(body.alamat() == null ? "" : body.alamat())
                .setProvinsi(body.provinsi() == null ? "" : body.provinsi())
                .setNegara(body.negara() == null ? "" : body.negara())
                .setKota(body.kota() == null ? "" : body.kota())
                .setShippingMethod(body.shippingMethod() == null ? "" : body.shippingMethod())
                .setShippingCost(body.shippingCost())
                .build())
                .map(CreateShippingAddressResponse::from));
    }

    @Override
    public Uni<UpdateShippingAddressResponse> updateShippingAddress(int id, UpdateShippingAddressRequest body) {
        return telemetryHelper.traceAndMetric("shippingAddress.updateShippingAddress", () -> shippingCommandService.updateShipping(pb.shipping_address.ShippingAddressCommand.UpdateShippingAddressRequest.newBuilder()
                .setShippingId(id)
                .setOrderId(body.orderId())
                .setAlamat(body.alamat() == null ? "" : body.alamat())
                .setProvinsi(body.provinsi() == null ? "" : body.provinsi())
                .setNegara(body.negara() == null ? "" : body.negara())
                .setKota(body.kota() == null ? "" : body.kota())
                .setShippingMethod(body.shippingMethod() == null ? "" : body.shippingMethod())
                .setShippingCost(body.shippingCost())
                .build())
                .map(UpdateShippingAddressResponse::from));
    }

    @Override
    public Uni<FindByIdShippingResponse> deleteShippingAddress(int id) {
        return telemetryHelper.traceAndMetric("shippingAddress.deleteShippingAddress", () -> shippingCommandService.trashedShipping(pb.shipping_address.ShippingAddressCommon.FindByIdShippingRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdShippingResponse::from));
    }

    @Override
    public Uni<FindByIdShippingResponse> restoreShippingAddress(int id) {
        return telemetryHelper.traceAndMetric("shippingAddress.restoreShippingAddress", () -> shippingCommandService.restoreShipping(pb.shipping_address.ShippingAddressCommon.FindByIdShippingRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdShippingResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteShippingAddressPermanent(int id) {
        return telemetryHelper.traceAndMetric("shippingAddress.deleteShippingAddressPermanent", () -> shippingCommandService.deleteShippingPermanent(pb.shipping_address.ShippingAddressCommon.FindByIdShippingRequest.newBuilder()
                .setId(id)
                .build())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllShippingAddresses() {
        return telemetryHelper.traceAndMetric("shippingAddress.restoreAllShippingAddresses", () -> shippingCommandService.restoreAllShipping(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllShippingAddressesPermanent() {
        return telemetryHelper.traceAndMetric("shippingAddress.deleteAllShippingAddressesPermanent", () -> shippingCommandService.deleteAllShippingPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }
}
