package com.sanedge.gateway.dto;

import java.util.List;

public class BannerDto {
    @org.eclipse.microprofile.graphql.Name("BannerBannerResponse")
    public record BannerResponse(
            int bannerId,
            String name,
            String startDate,
            String endDate,
            String startTime,
            String endTime,
            boolean isActive,
            String createdAt,
            String updatedAt) {
        public static BannerResponse from(pb.banner.BannerCommon.BannerResponse proto) {
            return new BannerResponse(
                    proto.getBannerId(),
                    proto.getName(),
                    proto.getStartDate(),
                    proto.getEndDate(),
                    proto.getStartTime(),
                    proto.getEndTime(),
                    proto.getIsActive(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
        public static BannerResponse from(pb.banner.BannerCommon.BannerResponseDeleteAt proto) {
            return new BannerResponse(
                    proto.getBannerId(),
                    proto.getName(),
                    proto.getStartDate(),
                    proto.getEndDate(),
                    proto.getStartTime(),
                    proto.getEndTime(),
                    proto.getIsActive(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("BannerFindAllBannerResponse")
    public record FindAllBannerResponse(
            List<BannerResponse> data,
            String status,
            String message) {
        public static FindAllBannerResponse from(pb.banner.BannerCommon.ApiResponsePaginationBanner proto) {
            return new FindAllBannerResponse(
                    proto.getDataList().stream().map(BannerResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllBannerResponse from(pb.banner.BannerCommon.ApiResponsePaginationBannerDeleteAt proto) {
            return new FindAllBannerResponse(
                    proto.getDataList().stream().map(BannerResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("BannerFindByIdBannerResponse")
    public record FindByIdBannerResponse(
            BannerResponse data,
            String status,
            String message) {
        public static FindByIdBannerResponse from(pb.banner.BannerCommon.ApiResponseBanner proto) {
            return new FindByIdBannerResponse(
                    proto.hasData() ? BannerResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindByIdBannerResponse from(pb.banner.BannerCommon.ApiResponseBannerDeleteAt proto) {
            return new FindByIdBannerResponse(
                    proto.hasData() ? BannerResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("BannerCreateBannerRequest")
    public record CreateBannerRequest(
            String name,
            String startDate,
            String endDate,
            String startTime,
            String endTime,
            boolean isActive) {}

    @org.eclipse.microprofile.graphql.Name("BannerCreateBannerResponse")
    public record CreateBannerResponse(
            BannerResponse data,
            String status,
            String message) {
        public static CreateBannerResponse from(pb.banner.BannerCommon.ApiResponseBanner proto) {
            return new CreateBannerResponse(
                    proto.hasData() ? BannerResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("BannerUpdateBannerRequest")
    public record UpdateBannerRequest(
            String name,
            String startDate,
            String endDate,
            String startTime,
            String endTime,
            boolean isActive) {}

    @org.eclipse.microprofile.graphql.Name("BannerUpdateBannerResponse")
    public record UpdateBannerResponse(
            BannerResponse data,
            String status,
            String message) {
        public static UpdateBannerResponse from(pb.banner.BannerCommon.ApiResponseBanner proto) {
            return new UpdateBannerResponse(
                    proto.hasData() ? BannerResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("BannerSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.banner.BannerCommon.ApiResponseBannerDelete proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
        public static SimpleStatusMessageResponse from(pb.banner.BannerCommon.ApiResponseBannerAll proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }
}
