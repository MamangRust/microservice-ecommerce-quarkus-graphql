package com.sanedge.gateway.dto;

import java.util.List;

public class MerchantPolicyDto {
    @org.eclipse.microprofile.graphql.Name("MerchantPolicyMerchantPoliciesResponse")
    public record MerchantPoliciesResponse(
            int id,
            int merchantId,
            String policyType,
            String title,
            String description,
            String createdAt,
            String updatedAt,
            String merchantName) {
        public static MerchantPoliciesResponse from(pb.merchant_policy.MerchantPolicyCommon.MerchantPoliciesResponse proto) {
            return new MerchantPoliciesResponse(
                    proto.getId(),
                    proto.getMerchantId(),
                    proto.getPolicyType(),
                    proto.getTitle(),
                    proto.getDescription(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt(),
                    proto.getMerchantName()
            );
        }
        public static MerchantPoliciesResponse from(pb.merchant_policy.MerchantPolicyCommon.MerchantPoliciesResponseDeleteAt proto) {
            return new MerchantPoliciesResponse(
                    proto.getId(),
                    proto.getMerchantId(),
                    proto.getPolicyType(),
                    proto.getTitle(),
                    proto.getDescription(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt(),
                    proto.getMerchantName()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantPolicyFindAllMerchantPolicyResponse")
    public record FindAllMerchantPolicyResponse(
            List<MerchantPoliciesResponse> data,
            String status,
            String message) {
        public static FindAllMerchantPolicyResponse from(pb.merchant_policy.MerchantPolicyCommon.ApiResponsePaginationMerchantPolicies proto) {
            return new FindAllMerchantPolicyResponse(
                    proto.getDataList().stream().map(MerchantPoliciesResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllMerchantPolicyResponse from(pb.merchant_policy.MerchantPolicyCommon.ApiResponsePaginationMerchantPoliciesDeleteAt proto) {
            return new FindAllMerchantPolicyResponse(
                    proto.getDataList().stream().map(MerchantPoliciesResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantPolicyFindByIdMerchantPolicyResponse")
    public record FindByIdMerchantPolicyResponse(
            MerchantPoliciesResponse data,
            String status,
            String message) {
        public static FindByIdMerchantPolicyResponse from(pb.merchant_policy.MerchantPolicyCommon.ApiResponseMerchantPolicies proto) {
            return new FindByIdMerchantPolicyResponse(
                    proto.hasData() ? MerchantPoliciesResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindByIdMerchantPolicyResponse from(pb.merchant_policy.MerchantPolicyCommon.ApiResponseMerchantPoliciesDeleteAt proto) {
            return new FindByIdMerchantPolicyResponse(
                    proto.hasData() ? MerchantPoliciesResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantPolicyCreateMerchantPolicyRequest")
    public record CreateMerchantPolicyRequest(
            int merchantId,
            String policyType,
            String title,
            String description) {}

    @org.eclipse.microprofile.graphql.Name("MerchantPolicyCreateMerchantPolicyResponse")
    public record CreateMerchantPolicyResponse(
            MerchantPoliciesResponse data,
            String status,
            String message) {
        public static CreateMerchantPolicyResponse from(pb.merchant_policy.MerchantPolicyCommon.ApiResponseMerchantPolicies proto) {
            return new CreateMerchantPolicyResponse(
                    proto.hasData() ? MerchantPoliciesResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantPolicyUpdateMerchantPolicyRequest")
    public record UpdateMerchantPolicyRequest(
            int merchantId,
            String policyType,
            String title,
            String description) {}

    @org.eclipse.microprofile.graphql.Name("MerchantPolicyUpdateMerchantPolicyResponse")
    public record UpdateMerchantPolicyResponse(
            MerchantPoliciesResponse data,
            String status,
            String message) {
        public static UpdateMerchantPolicyResponse from(pb.merchant_policy.MerchantPolicyCommon.ApiResponseMerchantPolicies proto) {
            return new UpdateMerchantPolicyResponse(
                    proto.hasData() ? MerchantPoliciesResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantPolicySimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.merchant_policy.MerchantPolicyCommon.ApiResponseMerchantPoliciesDeleteAt proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }
}
