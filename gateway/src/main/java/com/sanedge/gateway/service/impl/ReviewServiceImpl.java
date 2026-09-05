package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.ReviewDto.CreateReviewRequest;
import com.sanedge.gateway.dto.ReviewDto.CreateReviewResponse;
import com.sanedge.gateway.dto.ReviewDto.FindAllReviewResponse;
import com.sanedge.gateway.dto.ReviewDto.FindByIdReviewResponse;
import com.sanedge.gateway.dto.ReviewDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ReviewDto.UpdateReviewRequest;
import com.sanedge.gateway.dto.ReviewDto.UpdateReviewResponse;
import com.sanedge.gateway.service.ReviewService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReviewServiceImpl implements ReviewService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("review")
    pb.review.MutinyReviewQueryServiceGrpc.MutinyReviewQueryServiceStub reviewQueryService;

    @GrpcClient("review")
    pb.review.MutinyReviewCommandServiceGrpc.MutinyReviewCommandServiceStub reviewCommandService;

    @Override
    public Uni<FindAllReviewResponse> listReviews(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("review.listReviews", () -> reviewQueryService.findAll(pb.review.ReviewQuery.FindAllReviewRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllReviewResponse::from));
    }

    @Override
    public Uni<FindAllReviewResponse> listReviewsByProduct(int productId, int page, int size, String search) {
        return telemetryHelper.traceAndMetric("review.listReviewsByProduct", () -> reviewQueryService.findByProduct(pb.review.ReviewQuery.FindAllReviewProductRequest.newBuilder()
                .setProductId(productId)
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllReviewResponse::from));
    }

    @Override
    public Uni<FindAllReviewResponse> listReviewsByMerchant(int merchantId, int page, int size, String search) {
        return telemetryHelper.traceAndMetric("review.listReviewsByMerchant", () -> reviewQueryService.findByMerchant(pb.review.ReviewQuery.FindAllReviewMerchantRequest.newBuilder()
                .setMerchantId(merchantId)
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllReviewResponse::from));
    }

    @Override
    public Uni<FindAllReviewResponse> listActiveReviews(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("review.listActiveReviews", () -> reviewQueryService.findByActive(pb.review.ReviewQuery.FindAllReviewRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllReviewResponse::from));
    }

    @Override
    public Uni<FindAllReviewResponse> listTrashedReviews(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("review.listTrashedReviews", () -> reviewQueryService.findByTrashed(pb.review.ReviewQuery.FindAllReviewRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllReviewResponse::from));
    }

    @Override
    public Uni<FindByIdReviewResponse> getReview(int id) {
        return telemetryHelper.traceAndMetric("review.getReview", () -> reviewQueryService.findById(pb.review.ReviewCommon.FindByIdReviewRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdReviewResponse::from));
    }

    @Override
    public Uni<CreateReviewResponse> createReview(CreateReviewRequest body) {
        return telemetryHelper.traceAndMetric("review.createReview", () -> reviewCommandService.create(pb.review.ReviewCommand.CreateReviewRequest.newBuilder()
                .setUserId(body.userId())
                .setProductId(body.productId())
                .setName(body.name() == null ? "" : body.name())
                .setComment(body.comment() == null ? "" : body.comment())
                .setRating(body.rating())
                .build())
                .map(CreateReviewResponse::from));
    }

    @Override
    public Uni<UpdateReviewResponse> updateReview(int id, UpdateReviewRequest body) {
        return telemetryHelper.traceAndMetric("review.updateReview", () -> reviewCommandService.update(pb.review.ReviewCommand.UpdateReviewRequest.newBuilder()
                .setReviewId(id)
                .setName(body.name() == null ? "" : body.name())
                .setComment(body.comment() == null ? "" : body.comment())
                .setRating(body.rating())
                .build())
                .map(UpdateReviewResponse::from));
    }

    @Override
    public Uni<FindByIdReviewResponse> deleteReview(int id) {
        return telemetryHelper.traceAndMetric("review.deleteReview", () -> reviewCommandService.trashedReview(pb.review.ReviewCommon.FindByIdReviewRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdReviewResponse::from));
    }

    @Override
    public Uni<FindByIdReviewResponse> restoreReview(int id) {
        return telemetryHelper.traceAndMetric("review.restoreReview", () -> reviewCommandService.restoreReview(pb.review.ReviewCommon.FindByIdReviewRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdReviewResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteReviewPermanent(int id) {
        return telemetryHelper.traceAndMetric("review.deleteReviewPermanent", () -> reviewCommandService.deleteReviewPermanent(pb.review.ReviewCommon.FindByIdReviewRequest.newBuilder()
                .setId(id)
                .build())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllReviews() {
        return telemetryHelper.traceAndMetric("review.restoreAllReviews", () -> reviewCommandService.restoreAllReview(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllReviewsPermanent() {
        return telemetryHelper.traceAndMetric("review.deleteAllReviewsPermanent", () -> reviewCommandService.deleteAllReviewPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }
}
