package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.ReviewDetailDto.CreateReviewDetailRequest;
import com.sanedge.gateway.dto.ReviewDetailDto.CreateReviewDetailResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.FindAllReviewDetailResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.FindByIdReviewDetailResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.UpdateReviewDetailRequest;
import com.sanedge.gateway.dto.ReviewDetailDto.UpdateReviewDetailResponse;
import com.sanedge.gateway.service.FileService;
import com.sanedge.gateway.service.ReviewDetailService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@ApplicationScoped
public class ReviewDetailServiceImpl implements ReviewDetailService {

    @Inject
    FileService fileService;

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("review_detail")
    pb.review_detail.MutinyReviewDetailQueryServiceGrpc.MutinyReviewDetailQueryServiceStub reviewDetailQueryService;

    @GrpcClient("review_detail")
    pb.review_detail.MutinyReviewDetailCommandServiceGrpc.MutinyReviewDetailCommandServiceStub reviewDetailCommandService;

    @Override
    public Uni<FindAllReviewDetailResponse> listReviewDetails(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("reviewDetail.listReviewDetails", () -> reviewDetailQueryService.findAll(pb.review.ReviewQuery.FindAllReviewRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllReviewDetailResponse::from));
    }

    @Override
    public Uni<FindAllReviewDetailResponse> listActiveReviewDetails(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("reviewDetail.listActiveReviewDetails", () -> reviewDetailQueryService.findByActive(pb.review.ReviewQuery.FindAllReviewRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllReviewDetailResponse::from));
    }

    @Override
    public Uni<FindAllReviewDetailResponse> listTrashedReviewDetails(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("reviewDetail.listTrashedReviewDetails", () -> reviewDetailQueryService.findByTrashed(pb.review.ReviewQuery.FindAllReviewRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllReviewDetailResponse::from));
    }

    @Override
    public Uni<FindByIdReviewDetailResponse> getReviewDetail(int id) {
        return telemetryHelper.traceAndMetric("reviewDetail.getReviewDetail", () -> reviewDetailQueryService.findById(pb.review_detail.ReviewDetailCommon.FindByIdReviewDetailRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdReviewDetailResponse::from));
    }

    @Override
    public Uni<CreateReviewDetailResponse> createReviewDetail(CreateReviewDetailRequest body) {
        return telemetryHelper.traceAndMetric("reviewDetail.createReviewDetail", () -> reviewDetailCommandService.create(pb.review_detail.ReviewDetailCommand.CreateReviewDetailRequest.newBuilder()
                .setReviewId(body.reviewId())
                .setType(body.type() == null ? "" : body.type())
                .setUrl(body.url() == null ? "" : body.url())
                .setCaption(body.caption() == null ? "" : body.caption())
                .build())
                .map(CreateReviewDetailResponse::from));
    }

    @Override
    public Uni<UpdateReviewDetailResponse> updateReviewDetail(int id, UpdateReviewDetailRequest body) {
        return telemetryHelper.traceAndMetric("reviewDetail.updateReviewDetail", () -> reviewDetailCommandService.update(pb.review_detail.ReviewDetailCommand.UpdateReviewDetailRequest.newBuilder()
                .setReviewDetailId(id)
                .setType(body.type() == null ? "" : body.type())
                .setUrl(body.url() == null ? "" : body.url())
                .setCaption(body.caption() == null ? "" : body.caption())
                .build())
                .map(UpdateReviewDetailResponse::from));
    }

    @Override
    public Uni<UpdateReviewDetailResponse> uploadReviewDetail(int id, FileUpload file) {
        return telemetryHelper.traceAndMetric("reviewDetail.uploadReviewDetail", () -> reviewDetailQueryService.findById(pb.review_detail.ReviewDetailCommon.FindByIdReviewDetailRequest.newBuilder()
                .setId(id).build())
                .flatMap(res -> {
                    if (!res.hasData()) {
                        return Uni.createFrom().failure(new Exception("Review detail not found"));
                    }
                    pb.review_detail.ReviewDetailCommon.ReviewDetailsResponse data = res.getData();
                    String filepath = "uploads/review_details/" + System.currentTimeMillis() + "_" + file.fileName();
                    String savedPath = fileService.createFileImage(file, filepath);
                    if (savedPath == null) {
                        return Uni.createFrom().failure(new Exception("Failed to save upload file"));
                    }
                    return reviewDetailCommandService.update(pb.review_detail.ReviewDetailCommand.UpdateReviewDetailRequest.newBuilder()
                            .setReviewDetailId(id)
                            .setType(data.getType())
                            .setUrl(savedPath)
                            .setCaption(data.getCaption())
                            .build());
                })
                .map(UpdateReviewDetailResponse::from));
    }

    @Override
    public Uni<FindByIdReviewDetailResponse> deleteReviewDetail(int id) {
        return telemetryHelper.traceAndMetric("reviewDetail.deleteReviewDetail", () -> reviewDetailCommandService.trashedReviewDetail(pb.review_detail.ReviewDetailCommon.FindByIdReviewDetailRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdReviewDetailResponse::from));
    }

    @Override
    public Uni<FindByIdReviewDetailResponse> restoreReviewDetail(int id) {
        return telemetryHelper.traceAndMetric("reviewDetail.restoreReviewDetail", () -> reviewDetailCommandService.restoreReviewDetail(pb.review_detail.ReviewDetailCommon.FindByIdReviewDetailRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdReviewDetailResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteReviewDetailPermanent(int id) {
        return telemetryHelper.traceAndMetric("reviewDetail.deleteReviewDetailPermanent", () -> reviewDetailCommandService.deleteReviewDetailPermanent(pb.review_detail.ReviewDetailCommon.FindByIdReviewDetailRequest.newBuilder()
                .setId(id)
                .build())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllReviewDetails() {
        return telemetryHelper.traceAndMetric("reviewDetail.restoreAllReviewDetails", () -> reviewDetailCommandService.restoreAllReviewDetail(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllReviewDetailsPermanent() {
        return telemetryHelper.traceAndMetric("reviewDetail.deleteAllReviewDetailsPermanent", () -> reviewDetailCommandService.deleteAllReviewDetailPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }
}
