package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.MerchantDto.CreateMerchantRequest;
import com.sanedge.gateway.dto.MerchantDto.CreateMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.FindAllMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.FindByIdMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantDto.TrashedMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.UpdateMerchantRequest;
import com.sanedge.gateway.dto.MerchantDto.UpdateMerchantResponse;
import com.sanedge.gateway.service.MerchantService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MerchantServiceImpl implements MerchantService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("merchant")
    pb.merchant.MutinyMerchantQueryServiceGrpc.MutinyMerchantQueryServiceStub merchantQueryService;

    @GrpcClient("merchant")
    pb.merchant.MutinyMerchantCommandServiceGrpc.MutinyMerchantCommandServiceStub merchantCommandService;

    @Override
    public Uni<FindAllMerchantResponse> listMerchants(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchant.listMerchants", () -> merchantQueryService.findAll(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantResponse> getMerchant(int id) {
        return telemetryHelper.traceAndMetric("merchant.getMerchant", () -> merchantQueryService.findById(pb.merchant.MerchantCommon.FindByIdMerchantRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantResponse::from));
    }

    @Override
    public Uni<FindAllMerchantResponse> listActiveMerchants(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchant.listActiveMerchants", () -> merchantQueryService.findByActive(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantResponse::from));
    }

    @Override
    public Uni<FindAllMerchantResponse> listTrashedMerchants(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchant.listTrashedMerchants", () -> merchantQueryService.findByTrashed(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantResponse::from));
    }

    @Override
    public Uni<CreateMerchantResponse> createMerchant(CreateMerchantRequest body) {
        return telemetryHelper.traceAndMetric("merchant.createMerchant", () -> merchantCommandService.create(pb.merchant.MerchantCommand.CreateMerchantRequest.newBuilder()
                .setUserId(body.userId())
                .setName(body.name() == null ? "" : body.name())
                .setDescription(body.description() == null ? "" : body.description())
                .setAddress(body.address() == null ? "" : body.address())
                .setContactEmail(body.contactEmail() == null ? "" : body.contactEmail())
                .setContactPhone(body.contactPhone() == null ? "" : body.contactPhone())
                .setStatus(body.status() == null ? "" : body.status())
                .build())
                .map(CreateMerchantResponse::from));
    }

    @Override
    public Uni<UpdateMerchantResponse> updateMerchant(int id, UpdateMerchantRequest body) {
        return telemetryHelper.traceAndMetric("merchant.updateMerchant", () -> merchantCommandService.update(pb.merchant.MerchantCommand.UpdateMerchantRequest.newBuilder()
                .setMerchantId(id)
                .setUserId(body.userId())
                .setName(body.name() == null ? "" : body.name())
                .setDescription(body.description() == null ? "" : body.description())
                .setAddress(body.address() == null ? "" : body.address())
                .setContactEmail(body.contactEmail() == null ? "" : body.contactEmail())
                .setContactPhone(body.contactPhone() == null ? "" : body.contactPhone())
                .setStatus(body.status() == null ? "" : body.status())
                .build())
                .map(UpdateMerchantResponse::from));
    }

    @Override
    public Uni<TrashedMerchantResponse> deleteMerchant(int id) {
        return telemetryHelper.traceAndMetric("merchant.deleteMerchant", () -> merchantCommandService.trashedMerchant(pb.merchant.MerchantCommon.FindByIdMerchantRequest.newBuilder()
                .setId(id)
                .build())
                .map(TrashedMerchantResponse::from));
    }

    @Override
    public Uni<TrashedMerchantResponse> restoreMerchant(int id) {
        return telemetryHelper.traceAndMetric("merchant.restoreMerchant", () -> merchantCommandService.restoreMerchant(pb.merchant.MerchantCommon.FindByIdMerchantRequest.newBuilder()
                .setId(id)
                .build())
                .map(TrashedMerchantResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteMerchantPermanent(int id) {
        return telemetryHelper.traceAndMetric("merchant.deleteMerchantPermanent", () -> merchantCommandService.deleteMerchantPermanent(pb.merchant.MerchantCommon.FindByIdMerchantRequest.newBuilder()
                .setId(id)
                .build())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllMerchants() {
        return telemetryHelper.traceAndMetric("merchant.restoreAllMerchants", () -> merchantCommandService.restoreAllMerchant(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantsPermanent() {
        return telemetryHelper.traceAndMetric("merchant.deleteAllMerchantsPermanent", () -> merchantCommandService.deleteAllMerchantPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }
}
