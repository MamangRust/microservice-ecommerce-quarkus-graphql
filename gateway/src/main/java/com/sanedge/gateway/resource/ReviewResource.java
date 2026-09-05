package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.ReviewDto.CreateReviewRequest;
import com.sanedge.gateway.dto.ReviewDto.CreateReviewResponse;
import com.sanedge.gateway.dto.ReviewDto.FindAllReviewResponse;
import com.sanedge.gateway.dto.ReviewDto.FindByIdReviewResponse;
import com.sanedge.gateway.dto.ReviewDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ReviewDto.UpdateReviewRequest;
import com.sanedge.gateway.dto.ReviewDto.UpdateReviewResponse;
import com.sanedge.gateway.service.ReviewService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class ReviewResource {

        @Inject
        ReviewService reviewService;

        @Query("listReviews")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllReviewResponse> listReviews(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return reviewService.listReviews(page, size, search);
        }

        @Query("listReviewsByProduct")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllReviewResponse> listReviewsByProduct(
                        @Name("productId") int productId,
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return reviewService.listReviewsByProduct(productId, page, size, search);
        }

        @Query("listReviewsByMerchant")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllReviewResponse> listReviewsByMerchant(
                        @Name("merchantId") int merchantId,
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return reviewService.listReviewsByMerchant(merchantId, page, size, search);
        }

        @Query("listActiveReviews")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllReviewResponse> listActiveReviews(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return reviewService.listActiveReviews(page, size, search);
        }

        @Query("listTrashedReviews")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllReviewResponse> listTrashedReviews(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return reviewService.listTrashedReviews(page, size, search);
        }

        @Query("getReview")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindByIdReviewResponse> getReview(@Name("id") int id) {
                return reviewService.getReview(id);
        }

        @Mutation("createReview")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<CreateReviewResponse> createReview(@Name("body") CreateReviewRequest body) {
                return reviewService.createReview(body);
        }

        @Mutation("updateReview")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<UpdateReviewResponse> updateReview(@Name("id") int id, @Name("body") UpdateReviewRequest body) {
                return reviewService.updateReview(id, body);
        }

        @Mutation("deleteReview")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<FindByIdReviewResponse> deleteReview(@Name("id") int id) {
                return reviewService.deleteReview(id);
        }

        @Mutation("restoreReview")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<FindByIdReviewResponse> restoreReview(@Name("id") int id) {
                return reviewService.restoreReview(id);
        }

        @Mutation("deleteReviewPermanent")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteReviewPermanent(@Name("id") int id) {
                return reviewService.deleteReviewPermanent(id);
        }

        @Mutation("restoreAllReviews")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> restoreAllReviews() {
                return reviewService.restoreAllReviews();
        }

        @Mutation("deleteAllReviewsPermanent")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteAllReviewsPermanent() {
                return reviewService.deleteAllReviewsPermanent();
        }
}
