package com.sanedge.gateway.dto;

import java.util.List;

public class ReviewDetailDto {
    @org.eclipse.microprofile.graphql.Name("ReviewDetailReviewDetailsResponse")
    public record ReviewDetailsResponse(
            int id,
            int reviewId,
            String type,
            String url,
            String caption,
            String createdAt,
            String updatedAt) {
        public static ReviewDetailsResponse from(pb.review_detail.ReviewDetailCommon.ReviewDetailsResponse proto) {
            return new ReviewDetailsResponse(
                    proto.getId(),
                    proto.getReviewId(),
                    proto.getType(),
                    proto.getUrl(),
                    proto.getCaption(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
        public static ReviewDetailsResponse from(pb.review_detail.ReviewDetailCommon.ReviewDetailsResponseDeleteAt proto) {
            return new ReviewDetailsResponse(
                    proto.getId(),
                    proto.getReviewId(),
                    proto.getType(),
                    proto.getUrl(),
                    proto.getCaption(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("ReviewDetailFindAllReviewDetailResponse")
    public record FindAllReviewDetailResponse(
            List<ReviewDetailsResponse> data,
            String status,
            String message) {
        public static FindAllReviewDetailResponse from(pb.review_detail.ReviewDetailCommon.ApiResponsePaginationReviewDetails proto) {
            return new FindAllReviewDetailResponse(
                    proto.getDataList().stream().map(ReviewDetailsResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllReviewDetailResponse from(pb.review_detail.ReviewDetailCommon.ApiResponsePaginationReviewDetailsDeleteAt proto) {
            return new FindAllReviewDetailResponse(
                    proto.getDataList().stream().map(ReviewDetailsResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("ReviewDetailFindByIdReviewDetailResponse")
    public record FindByIdReviewDetailResponse(
            ReviewDetailsResponse data,
            String status,
            String message) {
        public static FindByIdReviewDetailResponse from(pb.review_detail.ReviewDetailCommon.ApiResponseReviewDetail proto) {
            return new FindByIdReviewDetailResponse(
                    proto.hasData() ? ReviewDetailsResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindByIdReviewDetailResponse from(pb.review_detail.ReviewDetailCommon.ApiResponseReviewDetailDeleteAt proto) {
            return new FindByIdReviewDetailResponse(
                    proto.hasData() ? ReviewDetailsResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("ReviewDetailCreateReviewDetailRequest")
    public record CreateReviewDetailRequest(
            int reviewId,
            String type,
            String url,
            String caption) {}

    @org.eclipse.microprofile.graphql.Name("ReviewDetailCreateReviewDetailResponse")
    public record CreateReviewDetailResponse(
            ReviewDetailsResponse data,
            String status,
            String message) {
        public static CreateReviewDetailResponse from(pb.review_detail.ReviewDetailCommon.ApiResponseReviewDetail proto) {
            return new CreateReviewDetailResponse(
                    proto.hasData() ? ReviewDetailsResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("ReviewDetailUpdateReviewDetailRequest")
    public record UpdateReviewDetailRequest(
            int reviewId,
            String type,
            String url,
            String caption) {}

    @org.eclipse.microprofile.graphql.Name("ReviewDetailUpdateReviewDetailResponse")
    public record UpdateReviewDetailResponse(
            ReviewDetailsResponse data,
            String status,
            String message) {
        public static UpdateReviewDetailResponse from(pb.review_detail.ReviewDetailCommon.ApiResponseReviewDetail proto) {
            return new UpdateReviewDetailResponse(
                    proto.hasData() ? ReviewDetailsResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("ReviewDetailSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.review_detail.ReviewDetailCommon.ApiResponseReviewDetailDeleteAt proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }
}
