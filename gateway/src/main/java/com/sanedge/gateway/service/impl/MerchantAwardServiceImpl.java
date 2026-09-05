package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.MerchantAwardDto.CreateMerchantAwardRequest;
import com.sanedge.gateway.dto.MerchantAwardDto.CreateMerchantAwardResponse;
import com.sanedge.gateway.dto.MerchantAwardDto.FindAllMerchantAwardResponse;
import com.sanedge.gateway.dto.MerchantAwardDto.FindByIdMerchantAwardResponse;
import com.sanedge.gateway.dto.MerchantAwardDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantAwardDto.UpdateMerchantAwardRequest;
import com.sanedge.gateway.dto.MerchantAwardDto.UpdateMerchantAwardResponse;
import com.sanedge.gateway.service.MerchantAwardService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MerchantAwardServiceImpl implements MerchantAwardService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("merchant")
    pb.merchant_award.MutinyMerchantAwardQueryServiceGrpc.MutinyMerchantAwardQueryServiceStub merchantAwardQueryService;

    @GrpcClient("merchant")
    pb.merchant_award.MutinyMerchantAwardCommandServiceGrpc.MutinyMerchantAwardCommandServiceStub merchantAwardCommandService;

    @Override
    public Uni<FindAllMerchantAwardResponse> listMerchantAwards(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantAward.listMerchantAwards", () -> merchantAwardQueryService.findAll(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantAwardResponse::from));
    }

    @Override
    public Uni<FindAllMerchantAwardResponse> listActiveMerchantAwards(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantAward.listActiveMerchantAwards", () -> merchantAwardQueryService.findByActive(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantAwardResponse::from));
    }

    @Override
    public Uni<FindAllMerchantAwardResponse> listTrashedMerchantAwards(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantAward.listTrashedMerchantAwards", () -> merchantAwardQueryService.findByTrashed(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantAwardResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantAwardResponse> getMerchantAward(int id) {
        return telemetryHelper.traceAndMetric("merchantAward.getMerchantAward", () -> merchantAwardQueryService.findById(pb.merchant_award.MerchantAwardCommon.FindByIdMerchantAwardRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantAwardResponse::from));
    }

    @Override
    public Uni<CreateMerchantAwardResponse> createMerchantAward(CreateMerchantAwardRequest body) {
        return telemetryHelper.traceAndMetric("merchantAward.createMerchantAward", () -> merchantAwardCommandService.create(pb.merchant_award.MerchantAwardCommand.CreateMerchantAwardRequest.newBuilder()
                .setMerchantId(body.merchantId())
                .setTitle(body.title() == null ? "" : body.title())
                .setDescription(body.description() == null ? "" : body.description())
                .setIssuedBy(body.issuedBy() == null ? "" : body.issuedBy())
                .setIssueDate(body.issueDate() == null ? "" : body.issueDate())
                .setExpiryDate(body.expiryDate() == null ? "" : body.expiryDate())
                .setCertificateUrl(body.certificateUrl() == null ? "" : body.certificateUrl())
                .build())
                .map(CreateMerchantAwardResponse::from));
    }

    @Override
    public Uni<UpdateMerchantAwardResponse> updateMerchantAward(int id, UpdateMerchantAwardRequest body) {
        return telemetryHelper.traceAndMetric("merchantAward.updateMerchantAward", () -> merchantAwardCommandService.update(pb.merchant_award.MerchantAwardCommand.UpdateMerchantAwardRequest.newBuilder()
                .setMerchantCertificationId(id)
                .setTitle(body.title() == null ? "" : body.title())
                .setDescription(body.description() == null ? "" : body.description())
                .setIssuedBy(body.issuedBy() == null ? "" : body.issuedBy())
                .setIssueDate(body.issueDate() == null ? "" : body.issueDate())
                .setExpiryDate(body.expiryDate() == null ? "" : body.expiryDate())
                .setCertificateUrl(body.certificateUrl() == null ? "" : body.certificateUrl())
                .build())
                .map(UpdateMerchantAwardResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantAwardResponse> deleteMerchantAward(int id) {
        return telemetryHelper.traceAndMetric("merchantAward.deleteMerchantAward", () -> merchantAwardCommandService.trashedMerchantAward(pb.merchant_award.MerchantAwardCommon.FindByIdMerchantAwardRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantAwardResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantAwardResponse> restoreMerchantAward(int id) {
        return telemetryHelper.traceAndMetric("merchantAward.restoreMerchantAward", () -> merchantAwardCommandService.restoreMerchantAward(pb.merchant_award.MerchantAwardCommon.FindByIdMerchantAwardRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantAwardResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteMerchantAwardPermanent(int id) {
        return telemetryHelper.traceAndMetric("merchantAward.deleteMerchantAwardPermanent", () -> merchantAwardCommandService.deleteMerchantAwardPermanent(pb.merchant_award.MerchantAwardCommon.FindByIdMerchantAwardRequest.newBuilder()
                .setId(id)
                .build())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllMerchantAwards() {
        return telemetryHelper.traceAndMetric("merchantAward.restoreAllMerchantAwards", () -> merchantAwardCommandService.restoreAllMerchantAward(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantAwardsPermanent() {
        return telemetryHelper.traceAndMetric("merchantAward.deleteAllMerchantAwardsPermanent", () -> merchantAwardCommandService.deleteAllMerchantAwardPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }
}
