package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.ReviewDto.CreateReviewRequest;
import com.sanedge.gateway.dto.ReviewDto.CreateReviewResponse;
import com.sanedge.gateway.dto.ReviewDto.FindAllReviewResponse;
import com.sanedge.gateway.dto.ReviewDto.FindByIdReviewResponse;
import com.sanedge.gateway.dto.ReviewDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ReviewDto.UpdateReviewRequest;
import com.sanedge.gateway.dto.ReviewDto.UpdateReviewResponse;
import io.smallrye.mutiny.Uni;

public interface ReviewService {
    Uni<FindAllReviewResponse> listReviews(int page, int size, String search);
    Uni<FindAllReviewResponse> listReviewsByProduct(int productId, int page, int size, String search);
    Uni<FindAllReviewResponse> listReviewsByMerchant(int merchantId, int page, int size, String search);
    Uni<FindAllReviewResponse> listActiveReviews(int page, int size, String search);
    Uni<FindAllReviewResponse> listTrashedReviews(int page, int size, String search);
    Uni<FindByIdReviewResponse> getReview(int id);
    Uni<CreateReviewResponse> createReview(CreateReviewRequest body);
    Uni<UpdateReviewResponse> updateReview(int id, UpdateReviewRequest body);
    Uni<FindByIdReviewResponse> deleteReview(int id);
    Uni<FindByIdReviewResponse> restoreReview(int id);
    Uni<SimpleStatusMessageResponse> deleteReviewPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllReviews();
    Uni<SimpleStatusMessageResponse> deleteAllReviewsPermanent();
}
