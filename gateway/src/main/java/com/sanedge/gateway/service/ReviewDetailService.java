package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.ReviewDetailDto.CreateReviewDetailRequest;
import com.sanedge.gateway.dto.ReviewDetailDto.CreateReviewDetailResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.FindAllReviewDetailResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.FindByIdReviewDetailResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.UpdateReviewDetailRequest;
import com.sanedge.gateway.dto.ReviewDetailDto.UpdateReviewDetailResponse;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public interface ReviewDetailService {
    Uni<FindAllReviewDetailResponse> listReviewDetails(int page, int size, String search);
    Uni<FindAllReviewDetailResponse> listActiveReviewDetails(int page, int size, String search);
    Uni<FindAllReviewDetailResponse> listTrashedReviewDetails(int page, int size, String search);
    Uni<FindByIdReviewDetailResponse> getReviewDetail(int id);
    Uni<CreateReviewDetailResponse> createReviewDetail(CreateReviewDetailRequest body);
    Uni<UpdateReviewDetailResponse> updateReviewDetail(int id, UpdateReviewDetailRequest body);
    Uni<UpdateReviewDetailResponse> uploadReviewDetail(int id, FileUpload file);
    Uni<FindByIdReviewDetailResponse> deleteReviewDetail(int id);
    Uni<FindByIdReviewDetailResponse> restoreReviewDetail(int id);
    Uni<SimpleStatusMessageResponse> deleteReviewDetailPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllReviewDetails();
    Uni<SimpleStatusMessageResponse> deleteAllReviewDetailsPermanent();
}
