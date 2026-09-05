package com.sanedge.gateway.dto;

import java.util.List;

public class ShippingAddressDto {
    @org.eclipse.microprofile.graphql.Name("ShippingAddressShippingResponse")
    public record ShippingResponse(
            int id,
            int orderId,
            String alamat,
            String provinsi,
            String negara,
            String kota,
            String shippingMethod,
            int shippingCost,
            String createdAt,
            String updatedAt) {
        public static ShippingResponse from(pb.shipping_address.ShippingAddressCommon.ShippingResponse proto) {
            return new ShippingResponse(
                    proto.getId(),
                    proto.getOrderId(),
                    proto.getAlamat(),
                    proto.getProvinsi(),
                    proto.getNegara(),
                    proto.getKota(),
                    proto.getShippingMethod(),
                    proto.getShippingCost(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
        public static ShippingResponse from(pb.shipping_address.ShippingAddressCommon.ShippingResponseDeleteAt proto) {
            return new ShippingResponse(
                    proto.getId(),
                    proto.getOrderId(),
                    proto.getAlamat(),
                    proto.getProvinsi(),
                    proto.getNegara(),
                    proto.getKota(),
                    proto.getShippingMethod(),
                    proto.getShippingCost(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("ShippingAddressFindAllShippingResponse")
    public record FindAllShippingResponse(
            List<ShippingResponse> data,
            String status,
            String message) {
        public static FindAllShippingResponse from(pb.shipping_address.ShippingAddressCommon.ApiResponsePaginationShipping proto) {
            return new FindAllShippingResponse(
                    proto.getDataList().stream().map(ShippingResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllShippingResponse from(pb.shipping_address.ShippingAddressCommon.ApiResponsePaginationShippingDeleteAt proto) {
            return new FindAllShippingResponse(
                    proto.getDataList().stream().map(ShippingResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("ShippingAddressFindByIdShippingResponse")
    public record FindByIdShippingResponse(
            ShippingResponse data,
            String status,
            String message) {
        public static FindByIdShippingResponse from(pb.shipping_address.ShippingAddressCommon.ApiResponseShipping proto) {
            return new FindByIdShippingResponse(
                    proto.hasData() ? ShippingResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindByIdShippingResponse from(pb.shipping_address.ShippingAddressCommon.ApiResponseShippingDeleteAt proto) {
            return new FindByIdShippingResponse(
                    proto.hasData() ? ShippingResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("ShippingAddressCreateShippingAddressRequest")
    public record CreateShippingAddressRequest(
            int orderId,
            String alamat,
            String provinsi,
            String negara,
            String kota,
            String shippingMethod,
            int shippingCost) {}

    @org.eclipse.microprofile.graphql.Name("ShippingAddressCreateShippingAddressResponse")
    public record CreateShippingAddressResponse(
            ShippingResponse data,
            String status,
            String message) {
        public static CreateShippingAddressResponse from(pb.shipping_address.ShippingAddressCommon.ApiResponseShipping proto) {
            return new CreateShippingAddressResponse(
                    proto.hasData() ? ShippingResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("ShippingAddressUpdateShippingAddressRequest")
    public record UpdateShippingAddressRequest(
            int orderId,
            String alamat,
            String provinsi,
            String negara,
            String kota,
            String shippingMethod,
            int shippingCost) {}

    @org.eclipse.microprofile.graphql.Name("ShippingAddressUpdateShippingAddressResponse")
    public record UpdateShippingAddressResponse(
            ShippingResponse data,
            String status,
            String message) {
        public static UpdateShippingAddressResponse from(pb.shipping_address.ShippingAddressCommon.ApiResponseShipping proto) {
            return new UpdateShippingAddressResponse(
                    proto.hasData() ? ShippingResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("ShippingAddressSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.shipping_address.ShippingAddressCommon.ApiResponseShippingDeleteAt proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
        public static SimpleStatusMessageResponse from(pb.shipping_address.ShippingAddressCommon.ApiResponseShippingDelete proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }
}
