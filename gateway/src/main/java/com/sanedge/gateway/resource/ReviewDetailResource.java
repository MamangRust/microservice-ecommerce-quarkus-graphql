package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.sanedge.gateway.dto.ReviewDetailDto.CreateReviewDetailRequest;
import com.sanedge.gateway.dto.ReviewDetailDto.CreateReviewDetailResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.FindAllReviewDetailResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.FindByIdReviewDetailResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ReviewDetailDto.UpdateReviewDetailRequest;
import com.sanedge.gateway.dto.ReviewDetailDto.UpdateReviewDetailResponse;
import com.sanedge.gateway.service.ReviewDetailService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class ReviewDetailResource {

        @Inject
        ReviewDetailService reviewDetailService;

        @Query("listReviewDetails")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllReviewDetailResponse> listReviewDetails(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return reviewDetailService.listReviewDetails(page, size, search);
        }

        @Query("listActiveReviewDetails")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllReviewDetailResponse> listActiveReviewDetails(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return reviewDetailService.listActiveReviewDetails(page, size, search);
        }

        @Query("listTrashedReviewDetails")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllReviewDetailResponse> listTrashedReviewDetails(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return reviewDetailService.listTrashedReviewDetails(page, size, search);
        }

        @Query("getReviewDetail")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindByIdReviewDetailResponse> getReviewDetail(@Name("id") int id) {
                return reviewDetailService.getReviewDetail(id);
        }

        @Mutation("createReviewDetail")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<CreateReviewDetailResponse> createReviewDetail(@Name("body") CreateReviewDetailRequest body) {
                return reviewDetailService.createReviewDetail(body);
        }

        @Mutation("updateReviewDetail")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<UpdateReviewDetailResponse> updateReviewDetail(@Name("id") int id,
                        @Name("body") UpdateReviewDetailRequest body) {
                return reviewDetailService.updateReviewDetail(id, body);
        }

        @Mutation("uploadReviewDetail")
        @Description("Upload review detail image/video")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<UpdateReviewDetailResponse> uploadReviewDetail(
                        @Name("id") int id,
                        @Name("file") FileUpload file) {
                return reviewDetailService.uploadReviewDetail(id, file);
        }

        @Mutation("deleteReviewDetail")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<FindByIdReviewDetailResponse> deleteReviewDetail(@Name("id") int id) {
                return reviewDetailService.deleteReviewDetail(id);
        }

        @Mutation("restoreReviewDetail")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<FindByIdReviewDetailResponse> restoreReviewDetail(@Name("id") int id) {
                return reviewDetailService.restoreReviewDetail(id);
        }

        @Mutation("deleteReviewDetailPermanent")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteReviewDetailPermanent(@Name("id") int id) {
                return reviewDetailService.deleteReviewDetailPermanent(id);
        }

        @Mutation("restoreAllReviewDetails")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> restoreAllReviewDetails() {
                return reviewDetailService.restoreAllReviewDetails();
        }

        @Mutation("deleteAllReviewDetailsPermanent")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteAllReviewDetailsPermanent() {
                return reviewDetailService.deleteAllReviewDetailsPermanent();
        }
}
