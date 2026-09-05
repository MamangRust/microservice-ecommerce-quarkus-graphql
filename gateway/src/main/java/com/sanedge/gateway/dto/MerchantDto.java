package com.sanedge.gateway.dto;

import java.util.List;

public class MerchantDto {

    @org.eclipse.microprofile.graphql.Name("MerchantMerchantResponse")
    public record MerchantResponse(
            int id,
            int userId,
            String name,
            String description,
            String address,
            String contactEmail,
            String contactPhone,
            String status,
            String createdAt,
            String updatedAt) {
        public static MerchantResponse from(pb.merchant.MerchantCommon.MerchantResponse proto) {
            return new MerchantResponse(
                    proto.getId(),
                    proto.getUserId(),
                    proto.getName(),
                    proto.getDescription(),
                    proto.getAddress(),
                    proto.getContactEmail(),
                    proto.getContactPhone(),
                    proto.getStatus(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
        public static MerchantResponse from(pb.merchant.MerchantCommon.MerchantResponseDeleteAt proto) {
            return new MerchantResponse(
                    proto.getId(),
                    proto.getUserId(),
                    proto.getName(),
                    proto.getDescription(),
                    proto.getAddress(),
                    proto.getContactEmail(),
                    proto.getContactPhone(),
                    proto.getStatus(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantFindAllMerchantResponse")
    public record FindAllMerchantResponse(
            List<MerchantResponse> data,
            String status,
            String message) {
        public static FindAllMerchantResponse from(pb.merchant.MerchantCommon.ApiResponsePaginationMerchant proto) {
            return new FindAllMerchantResponse(
                    proto.getDataList().stream().map(MerchantResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllMerchantResponse from(pb.merchant.MerchantCommon.ApiResponsePaginationMerchantDeleteAt proto) {
            return new FindAllMerchantResponse(
                    proto.getDataList().stream().map(MerchantResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantFindByIdMerchantResponse")
    public record FindByIdMerchantResponse(
            MerchantResponse data,
            String status,
            String message) {
        public static FindByIdMerchantResponse from(pb.merchant.MerchantCommon.ApiResponseMerchant proto) {
            return new FindByIdMerchantResponse(
                    proto.hasData() ? MerchantResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindByIdMerchantResponse from(pb.merchant.MerchantCommon.ApiResponseMerchantDeleteAt proto) {
            return new FindByIdMerchantResponse(
                    proto.hasData() ? MerchantResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantCreateMerchantRequest")
    public record CreateMerchantRequest(
            int userId,
            String name,
            String description,
            String address,
            String contactEmail,
            String contactPhone,
            String status) {}

    @org.eclipse.microprofile.graphql.Name("MerchantCreateMerchantResponse")
    public record CreateMerchantResponse(
            MerchantResponse data,
            String status,
            String message) {
        public static CreateMerchantResponse from(pb.merchant.MerchantCommon.ApiResponseMerchant proto) {
            return new CreateMerchantResponse(
                    proto.hasData() ? MerchantResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantUpdateMerchantRequest")
    public record UpdateMerchantRequest(
            int userId,
            String name,
            String description,
            String address,
            String contactEmail,
            String contactPhone,
            String status) {}

    @org.eclipse.microprofile.graphql.Name("MerchantUpdateMerchantResponse")
    public record UpdateMerchantResponse(
            MerchantResponse data,
            String status,
            String message) {
        public static UpdateMerchantResponse from(pb.merchant.MerchantCommon.ApiResponseMerchant proto) {
            return new UpdateMerchantResponse(
                    proto.hasData() ? MerchantResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantTrashedMerchantResponse")
    public record TrashedMerchantResponse(
            MerchantResponse data,
            String status,
            String message) {
        public static TrashedMerchantResponse from(pb.merchant.MerchantCommon.ApiResponseMerchantDeleteAt proto) {
            return new TrashedMerchantResponse(
                    proto.hasData() ? MerchantResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static TrashedMerchantResponse from(pb.merchant.MerchantCommon.ApiResponseMerchant proto) {
            return new TrashedMerchantResponse(
                    proto.hasData() ? MerchantResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.merchant.MerchantCommon.ApiResponseMerchantDelete proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
        public static SimpleStatusMessageResponse from(pb.merchant.MerchantCommon.ApiResponseMerchantAll proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }
}
