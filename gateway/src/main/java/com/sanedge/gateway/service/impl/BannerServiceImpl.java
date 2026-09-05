package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.BannerDto.CreateBannerRequest;
import com.sanedge.gateway.dto.BannerDto.CreateBannerResponse;
import com.sanedge.gateway.dto.BannerDto.FindAllBannerResponse;
import com.sanedge.gateway.dto.BannerDto.FindByIdBannerResponse;
import com.sanedge.gateway.dto.BannerDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.BannerDto.UpdateBannerRequest;
import com.sanedge.gateway.dto.BannerDto.UpdateBannerResponse;
import com.sanedge.gateway.service.BannerService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BannerServiceImpl implements BannerService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("banner")
    pb.banner.MutinyBannerQueryServiceGrpc.MutinyBannerQueryServiceStub bannerQueryService;

    @GrpcClient("banner")
    pb.banner.MutinyBannerCommandServiceGrpc.MutinyBannerCommandServiceStub bannerCommandService;

    @Override
    public Uni<FindAllBannerResponse> listBanners(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("banner.listBanners",
                () -> bannerQueryService.findAll(pb.banner.BannerQuery.FindAllBannerRequest.newBuilder()
                        .setPage(page)
                        .setPageSize(size)
                        .setSearch(search == null ? "" : search)
                        .build())
                        .map(FindAllBannerResponse::from));
    }

    @Override
    public Uni<FindAllBannerResponse> listActiveBanners(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("banner.listActiveBanners",
                () -> bannerQueryService.findByActive(pb.banner.BannerQuery.FindAllBannerRequest.newBuilder()
                        .setPage(page)
                        .setPageSize(size)
                        .setSearch(search == null ? "" : search)
                        .build())
                        .map(FindAllBannerResponse::from));
    }

    @Override
    public Uni<FindAllBannerResponse> listTrashedBanners(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("banner.listTrashedBanners",
                () -> bannerQueryService.findByTrashed(pb.banner.BannerQuery.FindAllBannerRequest.newBuilder()
                        .setPage(page)
                        .setPageSize(size)
                        .setSearch(search == null ? "" : search)
                        .build())
                        .map(FindAllBannerResponse::from));
    }

    @Override
    public Uni<FindByIdBannerResponse> getBanner(int id) {
        return telemetryHelper.traceAndMetric("banner.getBanner",
                () -> bannerQueryService.findById(pb.banner.BannerCommon.FindByIdBannerRequest.newBuilder()
                        .setId(id)
                        .build())
                        .map(FindByIdBannerResponse::from));
    }

    @Override
    public Uni<CreateBannerResponse> createBanner(CreateBannerRequest body) {
        return telemetryHelper.traceAndMetric("banner.createBanner",
                () -> bannerCommandService.create(pb.banner.BannerCommand.CreateBannerRequest.newBuilder()
                        .setName(body.name() == null ? "" : body.name())
                        .setStartDate(body.startDate() == null ? "" : body.startDate())
                        .setEndDate(body.endDate() == null ? "" : body.endDate())
                        .setStartTime(body.startTime() == null ? "" : body.startTime())
                        .setEndTime(body.endTime() == null ? "" : body.endTime())
                        .setIsActive(body.isActive())
                        .build())
                        .map(CreateBannerResponse::from));
    }

    @Override
    public Uni<UpdateBannerResponse> updateBanner(int id, UpdateBannerRequest body) {
        return telemetryHelper.traceAndMetric("banner.updateBanner",
                () -> bannerCommandService.update(pb.banner.BannerCommand.UpdateBannerRequest.newBuilder()
                        .setBannerId(id)
                        .setName(body.name() == null ? "" : body.name())
                        .setStartDate(body.startDate() == null ? "" : body.startDate())
                        .setEndDate(body.endDate() == null ? "" : body.endDate())
                        .setStartTime(body.startTime() == null ? "" : body.startTime())
                        .setEndTime(body.endTime() == null ? "" : body.endTime())
                        .setIsActive(body.isActive())
                        .build())
                        .map(UpdateBannerResponse::from));
    }

    @Override
    public Uni<FindByIdBannerResponse> deleteBanner(int id) {
        return telemetryHelper.traceAndMetric("banner.deleteBanner",
                () -> bannerCommandService.trash(pb.banner.BannerCommon.FindByIdBannerRequest.newBuilder()
                        .setId(id)
                        .build())
                        .map(FindByIdBannerResponse::from));
    }

    @Override
    public Uni<FindByIdBannerResponse> restoreBanner(int id) {
        return telemetryHelper.traceAndMetric("banner.restoreBanner",
                () -> bannerCommandService.restore(pb.banner.BannerCommon.FindByIdBannerRequest.newBuilder()
                        .setId(id)
                        .build())
                        .map(FindByIdBannerResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteBannerPermanent(int id) {
        return telemetryHelper.traceAndMetric("banner.deleteBannerPermanent",
                () -> bannerCommandService.deletePermanent(pb.banner.BannerCommon.FindByIdBannerRequest.newBuilder()
                        .setId(id)
                        .build())
                        .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllBanners() {
        return telemetryHelper.traceAndMetric("banner.restoreAllBanners",
                () -> bannerCommandService.restoreAll(com.google.protobuf.Empty.getDefaultInstance())
                        .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllBannersPermanent() {
        return telemetryHelper.traceAndMetric("banner.deleteAllBannersPermanent",
                () -> bannerCommandService.deleteAll(com.google.protobuf.Empty.getDefaultInstance())
                        .map(SimpleStatusMessageResponse::from));
    }
}
