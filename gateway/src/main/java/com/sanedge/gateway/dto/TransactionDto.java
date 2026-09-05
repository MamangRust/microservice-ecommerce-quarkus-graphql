package com.sanedge.gateway.dto;

import java.util.List;

public class TransactionDto {

    @org.eclipse.microprofile.graphql.Name("TransactionTransactionResponse")
    public record TransactionResponse(
            int id,
            int orderId,
            int merchantId,
            String paymentMethod,
            int amount,
            String paymentStatus,
            String createdAt,
            String updatedAt) {
        public static TransactionResponse from(pb.transaction.TransactionCommon.TransactionResponse proto) {
            return new TransactionResponse(
                    proto.getId(),
                    proto.getOrderId(),
                    proto.getMerchantId(),
                    proto.getPaymentMethod(),
                    proto.getAmount(),
                    proto.getPaymentStatus(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
        public static TransactionResponse from(pb.transaction.TransactionCommon.TransactionResponseDeleteAt proto) {
            return new TransactionResponse(
                    proto.getId(),
                    proto.getOrderId(),
                    proto.getMerchantId(),
                    proto.getPaymentMethod(),
                    proto.getAmount(),
                    proto.getPaymentStatus(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionFindAllTransactionResponse")
    public record FindAllTransactionResponse(
            List<TransactionResponse> data,
            String status,
            String message) {
        public static FindAllTransactionResponse from(pb.transaction.TransactionCommon.ApiResponsePaginationTransaction proto) {
            return new FindAllTransactionResponse(
                    proto.getDataList().stream().map(TransactionResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllTransactionResponse from(pb.transaction.TransactionCommon.ApiResponsePaginationTransactionDeleteAt proto) {
            return new FindAllTransactionResponse(
                    proto.getDataList().stream().map(TransactionResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionFindByIdTransactionResponse")
    public record FindByIdTransactionResponse(
            TransactionResponse data,
            String status,
            String message) {
        public static FindByIdTransactionResponse from(pb.transaction.TransactionCommon.ApiResponseTransaction proto) {
            return new FindByIdTransactionResponse(
                    proto.hasData() ? TransactionResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindByIdTransactionResponse from(pb.transaction.TransactionCommon.ApiResponseTransactionDeleteAt proto) {
            return new FindByIdTransactionResponse(
                    proto.hasData() ? TransactionResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionCreateTransactionRequest")
    public record CreateTransactionRequest(
            int orderId,
            int merchantId,
            String paymentMethod,
            int amount,
            String paymentStatus) {}

    @org.eclipse.microprofile.graphql.Name("TransactionCreateTransactionResponse")
    public record CreateTransactionResponse(
            TransactionResponse data,
            String status,
            String message) {
        public static CreateTransactionResponse from(pb.transaction.TransactionCommon.ApiResponseTransaction proto) {
            return new CreateTransactionResponse(
                    proto.hasData() ? TransactionResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionUpdateTransactionRequest")
    public record UpdateTransactionRequest(
            int orderId,
            int merchantId,
            String paymentMethod,
            int amount,
            String paymentStatus) {}

    @org.eclipse.microprofile.graphql.Name("TransactionUpdateTransactionResponse")
    public record UpdateTransactionResponse(
            TransactionResponse data,
            String status,
            String message) {
        public static UpdateTransactionResponse from(pb.transaction.TransactionCommon.ApiResponseTransaction proto) {
            return new UpdateTransactionResponse(
                    proto.hasData() ? TransactionResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionTrashedTransactionResponse")
    public record TrashedTransactionResponse(
            TransactionResponse data,
            String status,
            String message) {
        public static TrashedTransactionResponse from(pb.transaction.TransactionCommon.ApiResponseTransactionDeleteAt proto) {
            return new TrashedTransactionResponse(
                    proto.hasData() ? TransactionResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.transaction.TransactionCommon.ApiResponseTransactionDelete proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
        public static SimpleStatusMessageResponse from(pb.transaction.TransactionCommon.ApiResponseTransactionAll proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }

    // STATS RECORDS
    @org.eclipse.microprofile.graphql.Name("TransactionMonthlyAmountSuccess")
    public record MonthlyAmountSuccess(
            String year,
            String month,
            int totalSuccess,
            int totalAmount) {
        public static MonthlyAmountSuccess from(pb.transaction.TransactionCommon.TransactionMonthlyAmountSuccess proto) {
            return new MonthlyAmountSuccess(proto.getYear(), proto.getMonth(), proto.getTotalSuccess(), proto.getTotalAmount());
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionMonthlyAmountFailed")
    public record MonthlyAmountFailed(
            String year,
            String month,
            int totalFailed,
            int totalAmount) {
        public static MonthlyAmountFailed from(pb.transaction.TransactionCommon.TransactionMonthlyAmountFailed proto) {
            return new MonthlyAmountFailed(proto.getYear(), proto.getMonth(), proto.getTotalFailed(), proto.getTotalAmount());
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionYearlyAmountSuccess")
    public record YearlyAmountSuccess(
            String year,
            int totalSuccess,
            int totalAmount) {
        public static YearlyAmountSuccess from(pb.transaction.TransactionCommon.TransactionYearlyAmountSuccess proto) {
            return new YearlyAmountSuccess(proto.getYear(), proto.getTotalSuccess(), proto.getTotalAmount());
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionYearlyAmountFailed")
    public record YearlyAmountFailed(
            String year,
            int totalFailed,
            int totalAmount) {
        public static YearlyAmountFailed from(pb.transaction.TransactionCommon.TransactionYearlyAmountFailed proto) {
            return new YearlyAmountFailed(proto.getYear(), proto.getTotalFailed(), proto.getTotalAmount());
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionMonthlyMethod")
    public record MonthlyMethod(
            String month,
            String paymentMethod,
            int totalTransactions,
            int totalAmount) {
        public static MonthlyMethod from(pb.transaction.TransactionCommon.TransactionMonthlyMethod proto) {
            return new MonthlyMethod(proto.getMonth(), proto.getPaymentMethod(), proto.getTotalTransactions(), proto.getTotalAmount());
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionYearlyMethod")
    public record YearlyMethod(
            String year,
            String paymentMethod,
            int totalTransactions,
            int totalAmount) {
        public static YearlyMethod from(pb.transaction.TransactionCommon.TransactionYearlyMethod proto) {
            return new YearlyMethod(proto.getYear(), proto.getPaymentMethod(), proto.getTotalTransactions(), proto.getTotalAmount());
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionApiResponseTransactionMonthAmountSuccess")
    public record ApiResponseTransactionMonthAmountSuccess(
            List<MonthlyAmountSuccess> data,
            String status,
            String message) {
        public static ApiResponseTransactionMonthAmountSuccess from(pb.transaction.TransactionCommon.ApiResponseTransactionMonthAmountSuccess proto) {
            return new ApiResponseTransactionMonthAmountSuccess(
                    proto.getDataList().stream().map(MonthlyAmountSuccess::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionApiResponseTransactionYearAmountSuccess")
    public record ApiResponseTransactionYearAmountSuccess(
            List<YearlyAmountSuccess> data,
            String status,
            String message) {
        public static ApiResponseTransactionYearAmountSuccess from(pb.transaction.TransactionCommon.ApiResponseTransactionYearAmountSuccess proto) {
            return new ApiResponseTransactionYearAmountSuccess(
                    proto.getDataList().stream().map(YearlyAmountSuccess::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionApiResponseTransactionMonthAmountFailed")
    public record ApiResponseTransactionMonthAmountFailed(
            List<MonthlyAmountFailed> data,
            String status,
            String message) {
        public static ApiResponseTransactionMonthAmountFailed from(pb.transaction.TransactionCommon.ApiResponseTransactionMonthAmountFailed proto) {
            return new ApiResponseTransactionMonthAmountFailed(
                    proto.getDataList().stream().map(MonthlyAmountFailed::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionApiResponseTransactionYearAmountFailed")
    public record ApiResponseTransactionYearAmountFailed(
            List<YearlyAmountFailed> data,
            String status,
            String message) {
        public static ApiResponseTransactionYearAmountFailed from(pb.transaction.TransactionCommon.ApiResponseTransactionYearAmountFailed proto) {
            return new ApiResponseTransactionYearAmountFailed(
                    proto.getDataList().stream().map(YearlyAmountFailed::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionApiResponseTransactionMonthPaymentMethod")
    public record ApiResponseTransactionMonthPaymentMethod(
            List<MonthlyMethod> data,
            String status,
            String message) {
        public static ApiResponseTransactionMonthPaymentMethod from(pb.transaction.TransactionCommon.ApiResponseTransactionMonthPaymentMethod proto) {
            return new ApiResponseTransactionMonthPaymentMethod(
                    proto.getDataList().stream().map(MonthlyMethod::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("TransactionApiResponseTransactionYearPaymentmethod")
    public record ApiResponseTransactionYearPaymentmethod(
            List<YearlyMethod> data,
            String status,
            String message) {
        public static ApiResponseTransactionYearPaymentmethod from(pb.transaction.TransactionCommon.ApiResponseTransactionYearPaymentmethod proto) {
            return new ApiResponseTransactionYearPaymentmethod(
                    proto.getDataList().stream().map(YearlyMethod::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }
}
