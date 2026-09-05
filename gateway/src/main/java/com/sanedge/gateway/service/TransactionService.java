package com.sanedge.gateway.service;

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
import io.smallrye.mutiny.Uni;

public interface TransactionService {
    Uni<FindAllTransactionResponse> listTransactions(int page, int size, String search);
    Uni<FindAllTransactionResponse> listTransactionsByMerchant(int merchantId, int page, int size, String search);
    Uni<FindByIdTransactionResponse> getTransaction(int id);
    Uni<FindByIdTransactionResponse> getTransactionByOrder(int orderId);
    Uni<FindAllTransactionResponse> listActiveTransactions(int page, int size, String search);
    Uni<FindAllTransactionResponse> listTrashedTransactions(int page, int size, String search);
    Uni<CreateTransactionResponse> createTransaction(CreateTransactionRequest body);
    Uni<UpdateTransactionResponse> updateTransaction(int id, UpdateTransactionRequest body);
    Uni<TrashedTransactionResponse> deleteTransaction(int id);
    Uni<TrashedTransactionResponse> restoreTransaction(int id);
    Uni<SimpleStatusMessageResponse> deleteTransactionPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllTransactions();
    Uni<SimpleStatusMessageResponse> deleteAllTransactionsPermanent();

    // Stats
    Uni<ApiResponseTransactionMonthAmountSuccess> getMonthlyAmountSuccess(int year, int month);
    Uni<ApiResponseTransactionYearAmountSuccess> getYearlyAmountSuccess(int year);
    Uni<ApiResponseTransactionMonthAmountFailed> getMonthlyAmountFailed(int year, int month);
    Uni<ApiResponseTransactionYearAmountFailed> getYearlyAmountFailed(int year);
    Uni<ApiResponseTransactionMonthPaymentMethod> getMonthlyTransactionMethodSuccess(int year, int month);
    Uni<ApiResponseTransactionYearPaymentmethod> getYearlyTransactionMethodSuccess(int year);
}
