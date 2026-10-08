package com.sanedge.order_item.service.impl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sanedge.common.domain.response.ApiResponse;
import com.sanedge.common.domain.response.ApiResponsePagination;
import com.sanedge.common.domain.response.PagedResult;
import com.sanedge.common.domain.response.PaginationMeta;
import com.sanedge.common.observability.TracingMetrics;
import com.sanedge.order_item.domain.requests.FindAllOrderItemRequest;
import com.sanedge.order_item.domain.response.OrderItemResponse;
import com.sanedge.order_item.domain.response.OrderItemResponseDeleteAt;
import com.sanedge.order_item.repository.OrderItemRepository;
import com.sanedge.order_item.service.OrderItemQueryService;

import io.opentelemetry.api.common.Attributes;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderItemQueryServiceImpl implements OrderItemQueryService {
    private static final Logger logger = LoggerFactory.getLogger(OrderItemQueryServiceImpl.class);

    private final OrderItemRepository orderItemRepository;
    private final TracingMetrics tracingMetrics;

    @Inject
    public OrderItemQueryServiceImpl(OrderItemRepository orderItemRepository, TracingMetrics tracingMetrics) {
        this.orderItemRepository = orderItemRepository;
        this.tracingMetrics = tracingMetrics;
    }

    @Override
    public Uni<ApiResponsePagination<List<OrderItemResponse>>> findAll(FindAllOrderItemRequest request) {
        logger.info("Finding all order items with request: {}", request);

        return tracingMetrics.traceAndMeasure("findAllOrderItems", "find_all_order_items",
                () -> orderItemRepository.findOrderItems(request)
                        .map(paged -> {
                            logger.info("Successfully retrieved {} order items", paged.getData().size());
                            return buildPaginatedResponse(paged, request.getPage(), request.getPageSize(),
                                    "Order items retrieved successfully", OrderItemResponse::from);
                        })
                        .onFailure().invoke(e -> logger.error("Error finding all order items", e)));
    }

    @Override
    public Uni<ApiResponsePagination<List<OrderItemResponseDeleteAt>>> findActive(FindAllOrderItemRequest request) {
        logger.info("Finding active order items with request: {}", request);

        return tracingMetrics.traceAndMeasure("findActiveOrderItems", "find_active_order_items",
                () -> orderItemRepository.findActiveOrderItems(request)
                        .map(paged -> {
                            logger.info("Successfully retrieved {} active order items", paged.getData().size());
                            return buildPaginatedResponse(paged, request.getPage(), request.getPageSize(),
                                    "Active order items retrieved successfully", OrderItemResponseDeleteAt::from);
                        })
                        .onFailure().invoke(e -> logger.error("Error finding active order items", e)));
    }

    @Override
    public Uni<ApiResponsePagination<List<OrderItemResponseDeleteAt>>> findTrashed(FindAllOrderItemRequest request) {
        logger.info("Finding trashed order items with request: {}", request);

        return tracingMetrics.traceAndMeasure("findTrashedOrderItems", "find_trashed_order_items",
                () -> orderItemRepository.findTrashedOrderItems(request)
                        .map(paged -> {
                            logger.info("Successfully retrieved {} trashed order items", paged.getData().size());
                            return buildPaginatedResponse(paged, request.getPage(), request.getPageSize(),
                                    "Trashed order items retrieved successfully", OrderItemResponseDeleteAt::from);
                        })
                        .onFailure().invoke(e -> logger.error("Error finding trashed order items", e)));
    }

    @Override
    public Uni<ApiResponse<List<OrderItemResponse>>> findByOrder(Long orderId) {
        logger.info("Finding order items by order ID: {}", orderId);

        return tracingMetrics.traceAndMeasure("findOrderItemByOrder", "find_order_items_by_order",
                Attributes.builder().put("order.id", orderId.toString()).build(),
                () -> orderItemRepository.findOrderItemByOrder(orderId)
                        .map(items -> {
                            List<OrderItemResponse> responses = items.stream()
                                    .map(OrderItemResponse::from)
                                    .collect(Collectors.toList());
                            logger.info("Successfully retrieved {} order items for order ID: {}", responses.size(),
                                    orderId);
                            return ApiResponse.success("Order items for order retrieved successfully", responses);
                        })
                        .onFailure()
                        .invoke(e -> logger.error("Error finding order items by order ID: {}", orderId, e)));
    }

    private <T, R> ApiResponsePagination<List<R>> buildPaginatedResponse(
            PagedResult<T> pagedResult,
            int pageParam,
            int sizeParam,
            String successMessage,
            Function<T, R> mapper) {

        List<R> data = pagedResult.getData().stream()
                .map(mapper)
                .collect(Collectors.toList());

        int totalRecords = pagedResult.getTotalRecords();
        int size = sizeParam > 0 ? sizeParam : 1;
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        PaginationMeta pagination = new PaginationMeta(pageParam, size, totalPages, totalRecords);

        return new ApiResponsePagination<>("success", successMessage, data, pagination);
    }
}