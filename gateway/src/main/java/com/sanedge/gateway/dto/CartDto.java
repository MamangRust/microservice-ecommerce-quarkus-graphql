package com.sanedge.gateway.dto;

import java.util.List;

public class CartDto {
    @org.eclipse.microprofile.graphql.Name("CartCartResponse")
    public record CartResponse(
            int id,
            int userId,
            int productId,
            String name,
            int price,
            String image,
            int quantity,
            int weight,
            String createdAt,
            String updatedAt) {
        public static CartResponse from(pb.cart.CartCommon.CartResponse proto) {
            return new CartResponse(
                    proto.getId(),
                    proto.getUserId(),
                    proto.getProductId(),
                    proto.getName(),
                    proto.getPrice(),
                    proto.getImage(),
                    proto.getQuantity(),
                    proto.getWeight(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("CartFindAllCartResponse")
    public record FindAllCartResponse(
            List<CartResponse> data,
            String status,
            String message) {
        public static FindAllCartResponse from(pb.cart.CartCommon.ApiResponsePaginationCart proto) {
            return new FindAllCartResponse(
                    proto.getDataList().stream().map(CartResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("CartFindByIdCartResponse")
    public record FindByIdCartResponse(
            CartResponse data,
            String status,
            String message) {
        public static FindByIdCartResponse from(pb.cart.CartCommon.ApiResponseCart proto) {
            return new FindByIdCartResponse(
                    proto.hasData() ? CartResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("CartCreateCartRequest")
    public record CreateCartRequest(
            int quantity,
            int productId,
            int userId) {}

    @org.eclipse.microprofile.graphql.Name("CartCreateCartResponse")
    public record CreateCartResponse(
            CartResponse data,
            String status,
            String message) {
        public static CreateCartResponse from(pb.cart.CartCommon.ApiResponseCart proto) {
            return new CreateCartResponse(
                    proto.hasData() ? CartResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("CartSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.cart.CartCommon.ApiResponseCartDelete proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
        public static SimpleStatusMessageResponse from(pb.cart.CartCommon.ApiResponseCartAll proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }
}
