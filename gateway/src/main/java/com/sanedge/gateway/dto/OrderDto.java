package com.sanedge.gateway.dto;

import java.util.List;

public class OrderDto {

    @org.eclipse.microprofile.graphql.Name("OrderCreateOrderItemRequest")
    public record CreateOrderItemRequest(
            int productId,
            int quantity,
            int price) {}

    @org.eclipse.microprofile.graphql.Name("OrderCreateShippingAddressRequest")
    public record CreateShippingAddressRequest(
            String alamat,
            String provinsi,
            String kota,
            String courier,
            String shippingMethod,
            int shippingCost,
            String negara) {}

    @org.eclipse.microprofile.graphql.Name("OrderUpdateOrderItemRequest")
    public record UpdateOrderItemRequest(
            int orderItemId,
            int productId,
            int quantity,
            int price) {}

    @org.eclipse.microprofile.graphql.Name("OrderUpdateShippingAddressRequest")
    public record UpdateShippingAddressRequest(
            int shippingId,
            String alamat,
            String provinsi,
            String kota,
            String courier,
            String shippingMethod,
            int shippingCost,
            String negara) {}

    @org.eclipse.microprofile.graphql.Name("OrderOrderResponse")
    public record OrderResponse(
            int id,
            int merchantId,
            int userId,
            int totalPrice,
            String createdAt,
            String updatedAt) {
        public static OrderResponse from(pb.order.OrderCommon.OrderResponse proto) {
            return new OrderResponse(
                    proto.getId(),
                    proto.getMerchantId(),
                    proto.getUserId(),
                    proto.getTotalPrice(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
        public static OrderResponse from(pb.order.OrderCommon.OrderResponseDeleteAt proto) {
            return new OrderResponse(
                    proto.getId(),
                    proto.getMerchantId(),
                    proto.getUserId(),
                    proto.getTotalPrice(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderFindAllOrderResponse")
    public record FindAllOrderResponse(
            List<OrderResponse> data,
            String status,
            String message) {
        public static FindAllOrderResponse from(pb.order.OrderCommon.ApiResponsePaginationOrder proto) {
            return new FindAllOrderResponse(
                    proto.getDataList().stream().map(OrderResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllOrderResponse from(pb.order.OrderCommon.ApiResponsePaginationOrderDeleteAt proto) {
            return new FindAllOrderResponse(
                    proto.getDataList().stream().map(OrderResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderFindByIdOrderResponse")
    public record FindByIdOrderResponse(
            OrderResponse data,
            String status,
            String message) {
        public static FindByIdOrderResponse from(pb.order.OrderCommon.ApiResponseOrder proto) {
            return new FindByIdOrderResponse(
                    proto.hasData() ? OrderResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindByIdOrderResponse from(pb.order.OrderCommon.ApiResponseOrderDeleteAt proto) {
            return new FindByIdOrderResponse(
                    proto.hasData() ? OrderResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderCreateOrderRequest")
    public record CreateOrderRequest(
            int merchantId,
            int userId,
            int totalPrice,
            List<CreateOrderItemRequest> items,
            CreateShippingAddressRequest shipping) {}

    @org.eclipse.microprofile.graphql.Name("OrderCreateOrderResponse")
    public record CreateOrderResponse(
            OrderResponse data,
            String status,
            String message) {
        public static CreateOrderResponse from(pb.order.OrderCommon.ApiResponseOrder proto) {
            return new CreateOrderResponse(
                    proto.hasData() ? OrderResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderUpdateOrderRequest")
    public record UpdateOrderRequest(
            int merchantId,
            int userId,
            int totalPrice,
            List<UpdateOrderItemRequest> items,
            UpdateShippingAddressRequest shipping) {}

    @org.eclipse.microprofile.graphql.Name("OrderUpdateOrderResponse")
    public record UpdateOrderResponse(
            OrderResponse data,
            String status,
            String message) {
        public static UpdateOrderResponse from(pb.order.OrderCommon.ApiResponseOrder proto) {
            return new UpdateOrderResponse(
                    proto.hasData() ? OrderResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.order.OrderCommon.ApiResponseOrderDelete proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
        public static SimpleStatusMessageResponse from(pb.order.OrderCommon.ApiResponseOrderAll proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }

    // STATS RECORDS
    @org.eclipse.microprofile.graphql.Name("OrderOrderMonthlyResponse")
    public record OrderMonthlyResponse(
            String month,
            int orderCount,
            long totalRevenue,
            int totalItemsSold) {
        public static OrderMonthlyResponse from(pb.order.OrderCommon.OrderMonthlyResponse proto) {
            return new OrderMonthlyResponse(
                    proto.getMonth(),
                    proto.getOrderCount(),
                    proto.getTotalRevenue(),
                    proto.getTotalItemsSold()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderApiResponseOrderMonthly")
    public record ApiResponseOrderMonthly(
            List<OrderMonthlyResponse> data,
            String status,
            String message) {
        public static ApiResponseOrderMonthly from(pb.order.OrderCommon.ApiResponseOrderMonthly proto) {
            return new ApiResponseOrderMonthly(
                    proto.getDataList().stream().map(OrderMonthlyResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderOrderYearlyResponse")
    public record OrderYearlyResponse(
            String year,
            int orderCount,
            long totalRevenue,
            int totalItemsSold,
            int activeCashiers,
            int uniqueProductsSold) {
        public static OrderYearlyResponse from(pb.order.OrderCommon.OrderYearlyResponse proto) {
            return new OrderYearlyResponse(
                    proto.getYear(),
                    proto.getOrderCount(),
                    proto.getTotalRevenue(),
                    proto.getTotalItemsSold(),
                    proto.getActiveCashiers(),
                    proto.getUniqueProductsSold()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("OrderApiResponseOrderYearly")
    public record ApiResponseOrderYearly(
            List<OrderYearlyResponse> data,
            String status,
            String message) {
        public static ApiResponseOrderYearly from(pb.order.OrderCommon.ApiResponseOrderYearly proto) {
            return new ApiResponseOrderYearly(
                    proto.getDataList().stream().map(OrderYearlyResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }
}
