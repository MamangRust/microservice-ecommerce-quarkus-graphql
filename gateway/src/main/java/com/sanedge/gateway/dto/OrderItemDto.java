package com.sanedge.gateway.dto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemDto {

    @org.eclipse.microprofile.graphql.Name("OrderItemOrderItemResponse")
    public record OrderItemResponse(
            int id,
            int orderId,
            int productId,
            int quantity,
            int price,
            String createdAt,
            String updatedAt) {
        public static OrderItemResponse from(pb.order_item.OrderItemCommon.OrderItemResponse proto) {
            return new OrderItemResponse(
                    proto.getId(),
                    proto.getOrderId(),
                    proto.getProductId(),
                    proto.getQuantity(),
                    proto.getPrice(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt());
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderItemOrderItemResponseDeleteAt")
    public record OrderItemResponseDeleteAt(
            int id,
            int orderId,
            int productId,
            int quantity,
            int price,
            String createdAt,
            String updatedAt,
            String deletedAt) {
        public static OrderItemResponseDeleteAt from(pb.order_item.OrderItemCommon.OrderItemResponseDeleteAt proto) {
            return new OrderItemResponseDeleteAt(
                    proto.getId(),
                    proto.getOrderId(),
                    proto.getProductId(),
                    proto.getQuantity(),
                    proto.getPrice(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt(),
                    proto.hasDeletedAt() ? proto.getDeletedAt().getValue() : null);
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderItemFindAllOrderItemResponse")
    public record FindAllOrderItemResponse(
            List<OrderItemResponse> data,
            String status,
            String message,
            PaginationMetaDto paginationMeta) {
        public static FindAllOrderItemResponse from(
                pb.order_item.OrderItemCommon.ApiResponsePaginationOrderItem proto) {
            return new FindAllOrderItemResponse(
                    proto.getDataList().stream().map(OrderItemResponse::from).collect(Collectors.toList()),
                    proto.getStatus(),
                    proto.getMessage(),
                    proto.hasPagination() ? PaginationMetaDto.from(proto.getPagination()) : null);
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderItemFindAllOrderItemDeleteAtResponse")
    public record FindAllOrderItemDeleteAtResponse(
            List<OrderItemResponseDeleteAt> data,
            String status,
            String message,
            PaginationMetaDto paginationMeta) {
        public static FindAllOrderItemDeleteAtResponse from(
                pb.order_item.OrderItemCommon.ApiResponsePaginationOrderItemDeleteAt proto) {
            return new FindAllOrderItemDeleteAtResponse(
                    proto.getDataList().stream().map(OrderItemResponseDeleteAt::from).collect(Collectors.toList()),
                    proto.getStatus(),
                    proto.getMessage(),
                    proto.hasPagination() ? PaginationMetaDto.from(proto.getPagination()) : null);
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderItemFindByOrderResponse")
    public record FindByOrderResponse(
            List<OrderItemResponse> data,
            String status,
            String message) {
        public static FindByOrderResponse from(pb.order_item.OrderItemCommon.ApiResponsesOrderItem proto) {
            return new FindByOrderResponse(
                    proto.getDataList().stream().map(OrderItemResponse::from).collect(Collectors.toList()),
                    proto.getStatus(),
                    proto.getMessage());
        }
    }
}
