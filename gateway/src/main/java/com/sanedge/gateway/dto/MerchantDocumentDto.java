package com.sanedge.gateway.dto;

import java.util.List;

public class MerchantDocumentDto {

    @org.eclipse.microprofile.graphql.Name("MerchantDocumentCreateMerchantDocumentBody")
    public record CreateMerchantDocumentBody(
            int merchantId,
            String documentType,
            String documentUrl) {
    }

    @org.eclipse.microprofile.graphql.Name("MerchantDocumentUpdateMerchantDocumentBody")
    public record UpdateMerchantDocumentBody(
            int merchantId,
            String documentType,
            String documentUrl,
            String note,
            String status) {
    }

    @org.eclipse.microprofile.graphql.Name("MerchantDocumentUpdateMerchantDocumentStatusBody")
    public record UpdateMerchantDocumentStatusBody(
            int merchantId,
            String note,
            String status) {
    }

    @org.eclipse.microprofile.graphql.Name("MerchantDocumentMerchantDocumentResponse")
    public record MerchantDocumentResponse(
            int documentId,
            int merchantId,
            String documentType,
            String documentUrl,
            String status,
            String note,
            String uploadedAt,
            String updatedAt) {
        public static MerchantDocumentResponse from(
                pb.merchant_document.MerchantDocumentCommon.MerchantDocument proto) {
            return new MerchantDocumentResponse(
                    proto.getDocumentId(),
                    proto.getMerchantId(),
                    proto.getDocumentType(),
                    proto.getDocumentUrl(),
                    proto.getStatus(),
                    proto.getNote(),
                    proto.getUploadedAt(),
                    proto.getUpdatedAt());
        }

        public static MerchantDocumentResponse from(
                pb.merchant_document.MerchantDocumentCommon.MerchantDocumentDeleteAt proto) {
            return new MerchantDocumentResponse(
                    proto.getDocumentId(),
                    proto.getMerchantId(),
                    proto.getDocumentType(),
                    proto.getDocumentUrl(),
                    proto.getStatus(),
                    proto.getNote(),
                    proto.getUploadedAt(),
                    proto.getUpdatedAt());
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantDocumentFindAllMerchantDocumentsResponse")
    public record FindAllMerchantDocumentsResponse(
            List<MerchantDocumentResponse> data,
            String status,
            String message) {
        public static FindAllMerchantDocumentsResponse from(
                pb.merchant_document.MerchantDocumentCommon.ApiResponsePaginationMerchantDocument proto) {
            return new FindAllMerchantDocumentsResponse(
                    proto.getDataList().stream().map(MerchantDocumentResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage());
        }

        public static FindAllMerchantDocumentsResponse from(
                pb.merchant_document.MerchantDocumentCommon.ApiResponsePaginationMerchantDocumentAt proto) {
            return new FindAllMerchantDocumentsResponse(
                    proto.getDataList().stream().map(MerchantDocumentResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage());
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantDocumentFindByIdMerchantDocumentResponse")
    public record FindByIdMerchantDocumentResponse(
            MerchantDocumentResponse data,
            String status,
            String message) {
        public static FindByIdMerchantDocumentResponse from(
                pb.merchant_document.MerchantDocumentCommon.ApiResponseMerchantDocument proto) {
            return new FindByIdMerchantDocumentResponse(
                    proto.hasData() ? MerchantDocumentResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage());
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantDocumentCreateMerchantDocumentResponse")
    public record CreateMerchantDocumentResponse(
            MerchantDocumentResponse data,
            String status,
            String message) {
        public static CreateMerchantDocumentResponse from(
                pb.merchant_document.MerchantDocumentCommon.ApiResponseMerchantDocument proto) {
            return new CreateMerchantDocumentResponse(
                    proto.hasData() ? MerchantDocumentResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage());
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantDocumentUpdateMerchantDocumentResponse")
    public record UpdateMerchantDocumentResponse(
            MerchantDocumentResponse data,
            String status,
            String message) {
        public static UpdateMerchantDocumentResponse from(
                pb.merchant_document.MerchantDocumentCommon.ApiResponseMerchantDocument proto) {
            return new UpdateMerchantDocumentResponse(
                    proto.hasData() ? MerchantDocumentResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage());
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantDocumentTrashedMerchantDocumentResponse")
    public record TrashedMerchantDocumentResponse(
            MerchantDocumentResponse data,
            String status,
            String message) {
        public static TrashedMerchantDocumentResponse from(
                pb.merchant_document.MerchantDocumentCommon.ApiResponseMerchantDocument proto) {
            return new TrashedMerchantDocumentResponse(
                    proto.hasData() ? MerchantDocumentResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage());
        }
    }

    @org.eclipse.microprofile.graphql.Name("MerchantDocumentSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(
                pb.merchant_document.MerchantDocumentCommon.ApiResponseMerchantDocumentDelete proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }

        public static SimpleStatusMessageResponse from(
                pb.merchant_document.MerchantDocumentCommon.ApiResponseMerchantDocumentAll proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }
}
