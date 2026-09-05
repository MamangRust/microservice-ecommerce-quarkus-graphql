package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.MerchantDetailDto.CreateMerchantDetailRequest;
import com.sanedge.gateway.dto.MerchantDetailDto.CreateMerchantDetailResponse;
import com.sanedge.gateway.dto.MerchantDetailDto.FindAllMerchantDetailResponse;
import com.sanedge.gateway.dto.MerchantDetailDto.FindByIdMerchantDetailResponse;
import com.sanedge.gateway.dto.MerchantDetailDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantDetailDto.UpdateMerchantDetailRequest;
import com.sanedge.gateway.dto.MerchantDetailDto.UpdateMerchantDetailResponse;
import com.sanedge.gateway.service.MerchantDetailService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MerchantDetailServiceImpl implements MerchantDetailService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("merchant")
    pb.merchant_detail.MutinyMerchantDetailQueryServiceGrpc.MutinyMerchantDetailQueryServiceStub merchantDetailQueryService;

    @GrpcClient("merchant")
    pb.merchant_detail.MutinyMerchantDetailCommandServiceGrpc.MutinyMerchantDetailCommandServiceStub merchantDetailCommandService;

    @Override
    public Uni<FindAllMerchantDetailResponse> listMerchantDetails(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantDetail.listMerchantDetails", () -> merchantDetailQueryService.findAll(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantDetailResponse::from));
    }

    @Override
    public Uni<FindAllMerchantDetailResponse> listActiveMerchantDetails(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantDetail.listActiveMerchantDetails", () -> merchantDetailQueryService.findByActive(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantDetailResponse::from));
    }

    @Override
    public Uni<FindAllMerchantDetailResponse> listTrashedMerchantDetails(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantDetail.listTrashedMerchantDetails", () -> merchantDetailQueryService.findByTrashed(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantDetailResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantDetailResponse> getMerchantDetail(int id) {
        return telemetryHelper.traceAndMetric("merchantDetail.getMerchantDetail", () -> merchantDetailQueryService.findById(pb.merchant_detail.MerchantDetailCommon.FindByIdMerchantDetailRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantDetailResponse::from));
    }

    @Override
    public Uni<CreateMerchantDetailResponse> createMerchantDetail(CreateMerchantDetailRequest body) {
        return telemetryHelper.traceAndMetric("merchantDetail.createMerchantDetail", () -> merchantDetailCommandService.create(pb.merchant_detail.MerchantDetailCommand.CreateMerchantDetailRequest.newBuilder()
                .setMerchantId(body.merchantId())
                .setDisplayName(body.displayName() == null ? "" : body.displayName())
                .setCoverImageUrl(body.coverImageUrl() == null ? "" : body.coverImageUrl())
                .setLogoUrl(body.logoUrl() == null ? "" : body.logoUrl())
                .setShortDescription(body.shortDescription() == null ? "" : body.shortDescription())
                .setWebsiteUrl(body.websiteUrl() == null ? "" : body.websiteUrl())
                .build())
                .map(CreateMerchantDetailResponse::from));
    }

    @Override
    public Uni<UpdateMerchantDetailResponse> updateMerchantDetail(int id, UpdateMerchantDetailRequest body) {
        return telemetryHelper.traceAndMetric("merchantDetail.updateMerchantDetail", () -> merchantDetailCommandService.update(pb.merchant_detail.MerchantDetailCommand.UpdateMerchantDetailRequest.newBuilder()
                .setMerchantDetailId(id)
                .setDisplayName(body.displayName() == null ? "" : body.displayName())
                .setCoverImageUrl(body.coverImageUrl() == null ? "" : body.coverImageUrl())
                .setLogoUrl(body.logoUrl() == null ? "" : body.logoUrl())
                .setShortDescription(body.shortDescription() == null ? "" : body.shortDescription())
                .setWebsiteUrl(body.websiteUrl() == null ? "" : body.websiteUrl())
                .build())
                .map(UpdateMerchantDetailResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantDetailResponse> deleteMerchantDetail(int id) {
        return telemetryHelper.traceAndMetric("merchantDetail.deleteMerchantDetail", () -> merchantDetailCommandService.trashedMerchantDetail(pb.merchant_detail.MerchantDetailCommon.FindByIdMerchantDetailRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantDetailResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantDetailResponse> restoreMerchantDetail(int id) {
        return telemetryHelper.traceAndMetric("merchantDetail.restoreMerchantDetail", () -> merchantDetailCommandService.restoreMerchantDetail(pb.merchant_detail.MerchantDetailCommon.FindByIdMerchantDetailRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantDetailResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteMerchantDetailPermanent(int id) {
        return telemetryHelper.traceAndMetric("merchantDetail.deleteMerchantDetailPermanent", () -> merchantDetailCommandService.deleteMerchantDetailPermanent(pb.merchant_detail.MerchantDetailCommon.FindByIdMerchantDetailRequest.newBuilder()
                .setId(id)
                .build())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllMerchantDetails() {
        return telemetryHelper.traceAndMetric("merchantDetail.restoreAllMerchantDetails", () -> merchantDetailCommandService.restoreAllMerchantDetail(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantDetailsPermanent() {
        return telemetryHelper.traceAndMetric("merchantDetail.deleteAllMerchantDetailsPermanent", () -> merchantDetailCommandService.deleteAllMerchantDetailPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }
}
