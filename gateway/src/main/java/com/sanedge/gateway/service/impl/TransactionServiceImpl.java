package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.TransactionDto.ApiResponseTransactionMonthAmountFailed;
import com.sanedge.gateway.dto.TransactionDto.ApiResponseTransactionMonthAmountSuccess;
import com.sanedge.gateway.dto.TransactionDto.ApiResponseTransactionMonthPaymentMethod;
import com.sanedge.gateway.dto.TransactionDto.ApiResponseTransactionYearAmountFailed;
import com.sanedge.gateway.dto.TransactionDto.ApiResponseTransactionYearAmountSuccess;
import com.sanedge.gateway.dto.TransactionDto.ApiResponseTransactionYearPaymentmethod;
import com.sanedge.gateway.dto.TransactionDto.CreateTransactionRequest;
import com.sanedge.gateway.dto.TransactionDto.CreateTransactionResponse;
import com.sanedge.gateway.dto.TransactionDto.FindAllTransactionResponse;
import com.sanedge.gateway.dto.TransactionDto.FindByIdTransactionResponse;
import com.sanedge.gateway.dto.TransactionDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.TransactionDto.TrashedTransactionResponse;
import com.sanedge.gateway.dto.TransactionDto.UpdateTransactionRequest;
import com.sanedge.gateway.dto.TransactionDto.UpdateTransactionResponse;
import com.sanedge.gateway.service.TransactionService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionServiceImpl implements TransactionService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("transaction")
    pb.transaction.MutinyTransactionQueryServiceGrpc.MutinyTransactionQueryServiceStub transactionQueryService;

    @GrpcClient("transaction")
    pb.transaction.MutinyTransactionCommandServiceGrpc.MutinyTransactionCommandServiceStub transactionCommandService;

    @GrpcClient("statsreader")
    pb.transaction.stats.MutinyTransactionAmountServiceGrpc.MutinyTransactionAmountServiceStub transactionAmountService;

    @GrpcClient("statsreader")
    pb.transaction.stats.MutinyTransactionMethodServiceGrpc.MutinyTransactionMethodServiceStub transactionMethodService;

    @Override
    public Uni<FindAllTransactionResponse> listTransactions(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("transaction.listTransactions", () -> transactionQueryService.findAllTransactions(pb.transaction.TransactionQuery.FindAllTransactionRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllTransactionResponse::from));
    }

    @Override
    public Uni<FindAllTransactionResponse> listTransactionsByMerchant(int merchantId, int page, int size, String search) {
        return telemetryHelper.traceAndMetric("transaction.listTransactionsByMerchant", () -> transactionQueryService.findByMerchant(pb.transaction.TransactionQuery.FindAllTransactionByMerchantRequest.newBuilder()
                .setMerchantId(merchantId)
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllTransactionResponse::from));
    }

    @Override
    public Uni<FindByIdTransactionResponse> getTransaction(int id) {
        return telemetryHelper.traceAndMetric("transaction.getTransaction", () -> transactionQueryService.findById(pb.transaction.TransactionCommon.FindByIdTransactionRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdTransactionResponse::from));
    }

    @Override
    public Uni<FindByIdTransactionResponse> getTransactionByOrder(int orderId) {
        return telemetryHelper.traceAndMetric("transaction.getTransactionByOrder", () -> transactionQueryService.findByOrderId(pb.transaction.TransactionQuery.FindByOrderIdTransactionRequest.newBuilder()
                .setOrderId(orderId)
                .build())
                .map(FindByIdTransactionResponse::from));
    }

    @Override
    public Uni<FindAllTransactionResponse> listActiveTransactions(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("transaction.listActiveTransactions", () -> transactionQueryService.findByActive(pb.transaction.TransactionQuery.FindAllTransactionRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllTransactionResponse::from));
    }

    @Override
    public Uni<FindAllTransactionResponse> listTrashedTransactions(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("transaction.listTrashedTransactions", () -> transactionQueryService.findByTrashed(pb.transaction.TransactionQuery.FindAllTransactionRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllTransactionResponse::from));
    }

    @Override
    public Uni<CreateTransactionResponse> createTransaction(CreateTransactionRequest body) {
        return telemetryHelper.traceAndMetric("transaction.createTransaction", () -> transactionCommandService.create(pb.transaction.TransactionCommand.CreateTransactionRequest.newBuilder()
                .setOrderId(body.orderId())
                .setMerchantId(body.merchantId())
                .setPaymentMethod(body.paymentMethod() == null ? "" : body.paymentMethod())
                .setAmount(body.amount())
                .setPaymentStatus(body.paymentStatus() == null ? "" : body.paymentStatus())
                .build())
                .map(CreateTransactionResponse::from));
    }

    @Override
    public Uni<UpdateTransactionResponse> updateTransaction(int id, UpdateTransactionRequest body) {
        return telemetryHelper.traceAndMetric("transaction.updateTransaction", () -> transactionCommandService.update(pb.transaction.TransactionCommand.UpdateTransactionRequest.newBuilder()
                .setTransactionId(id)
                .setOrderId(body.orderId())
                .setMerchantId(body.merchantId())
                .setPaymentMethod(body.paymentMethod() == null ? "" : body.paymentMethod())
                .setAmount(body.amount())
                .setPaymentStatus(body.paymentStatus() == null ? "" : body.paymentStatus())
                .build())
                .map(UpdateTransactionResponse::from));
    }

    @Override
    public Uni<TrashedTransactionResponse> deleteTransaction(int id) {
        return telemetryHelper.traceAndMetric("transaction.deleteTransaction", () -> transactionCommandService.trashedTransaction(pb.transaction.TransactionCommon.FindByIdTransactionRequest.newBuilder()
                .setId(id)
                .build())
                .map(TrashedTransactionResponse::from));
    }

    @Override
    public Uni<TrashedTransactionResponse> restoreTransaction(int id) {
        return telemetryHelper.traceAndMetric("transaction.restoreTransaction", () -> transactionCommandService.restoreTransaction(pb.transaction.TransactionCommon.FindByIdTransactionRequest.newBuilder()
                .setId(id)
                .build())
                .map(TrashedTransactionResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteTransactionPermanent(int id) {
        return telemetryHelper.traceAndMetric("transaction.deleteTransactionPermanent", () -> transactionCommandService.deleteTransactionPermanent(pb.transaction.TransactionCommon.FindByIdTransactionRequest.newBuilder()
                .setId(id)
                .build())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllTransactions() {
        return telemetryHelper.traceAndMetric("transaction.restoreAllTransactions", () -> transactionCommandService.restoreAllTransaction(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllTransactionsPermanent() {
        return telemetryHelper.traceAndMetric("transaction.deleteAllTransactionsPermanent", () -> transactionCommandService.deleteAllTransactionPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    // Stats
    @Override
    public Uni<ApiResponseTransactionMonthAmountSuccess> getMonthlyAmountSuccess(int year, int month) {
        return telemetryHelper.traceAndMetric("transaction.getMonthlyAmountSuccess", () -> transactionAmountService.getMonthlyAmountSuccess(pb.transaction.stats.TransactionAmount.MonthAmountTransactionRequest.newBuilder()
                .setYear(year)
                .setMonth(month)
                .build())
                .map(ApiResponseTransactionMonthAmountSuccess::from));
    }

    @Override
    public Uni<ApiResponseTransactionYearAmountSuccess> getYearlyAmountSuccess(int year) {
        return telemetryHelper.traceAndMetric("transaction.getYearlyAmountSuccess", () -> transactionAmountService.getYearlyAmountSuccess(pb.transaction.stats.TransactionAmount.YearAmountTransactionRequest.newBuilder()
                .setYear(year)
                .build())
                .map(ApiResponseTransactionYearAmountSuccess::from));
    }

    @Override
    public Uni<ApiResponseTransactionMonthAmountFailed> getMonthlyAmountFailed(int year, int month) {
        return telemetryHelper.traceAndMetric("transaction.getMonthlyAmountFailed", () -> transactionAmountService.getMonthlyAmountFailed(pb.transaction.stats.TransactionAmount.MonthAmountTransactionRequest.newBuilder()
                .setYear(year)
                .setMonth(month)
                .build())
                .map(ApiResponseTransactionMonthAmountFailed::from));
    }

    @Override
    public Uni<ApiResponseTransactionYearAmountFailed> getYearlyAmountFailed(int year) {
        return telemetryHelper.traceAndMetric("transaction.getYearlyAmountFailed", () -> transactionAmountService.getYearlyAmountFailed(pb.transaction.stats.TransactionAmount.YearAmountTransactionRequest.newBuilder()
                .setYear(year)
                .build())
                .map(ApiResponseTransactionYearAmountFailed::from));
    }

    @Override
    public Uni<ApiResponseTransactionMonthPaymentMethod> getMonthlyTransactionMethodSuccess(int year, int month) {
        return telemetryHelper.traceAndMetric("transaction.getMonthlyTransactionMethodSuccess", () -> transactionMethodService.getMonthlyTransactionMethodSuccess(pb.transaction.stats.TransactionMethod.MonthMethodTransactionRequest.newBuilder()
                .setYear(year)
                .setMonth(month)
                .build())
                .map(ApiResponseTransactionMonthPaymentMethod::from));
    }

    @Override
    public Uni<ApiResponseTransactionYearPaymentmethod> getYearlyTransactionMethodSuccess(int year) {
        return telemetryHelper.traceAndMetric("transaction.getYearlyTransactionMethodSuccess", () -> transactionMethodService.getYearlyTransactionMethodSuccess(pb.transaction.stats.TransactionMethod.YearMethodTransactionRequest.newBuilder()
                .setYear(year)
                .build())
                .map(ApiResponseTransactionYearPaymentmethod::from));
    }
}
