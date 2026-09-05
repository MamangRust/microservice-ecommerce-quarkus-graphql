package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.TransactionDto.*;
import com.sanedge.gateway.service.TransactionService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class TransactionResource {

    @Inject
    TransactionService transactionService;

    @Query("listTransactions")
    @Description("List all transactions")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllTransactionResponse> listTransactions(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return transactionService.listTransactions(page, size, search);
    }

    @Query("listTransactionsByMerchant")
    @Description("List transactions by merchant ID")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindAllTransactionResponse> listTransactionsByMerchant(
            @Name("merchantId") int merchantId,
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return transactionService.listTransactionsByMerchant(merchantId, page, size, search);
    }

    @Query("getTransactionByOrder")
    @Description("Get transaction by order ID")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindByIdTransactionResponse> getTransactionByOrder(@Name("orderId") int orderId) {
        return transactionService.getTransactionByOrder(orderId);
    }

    @Query("listActiveTransactions")
    @Description("List active transactions")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindAllTransactionResponse> listActiveTransactions(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return transactionService.listActiveTransactions(page, size, search);
    }

    @Query("listTrashedTransactions")
    @Description("List trashed transactions")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindAllTransactionResponse> listTrashedTransactions(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return transactionService.listTrashedTransactions(page, size, search);
    }

    @Query("getTransaction")
    @Description("Get transaction by ID")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindByIdTransactionResponse> getTransaction(@Name("id") int id) {
        return transactionService.getTransaction(id);
    }

    @Mutation("createTransaction")
    @Description("Create a new transaction")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<CreateTransactionResponse> createTransaction(@Name("body") CreateTransactionRequest body) {
        return transactionService.createTransaction(body);
    }

    @Mutation("updateTransaction")
    @Description("Update transaction")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<UpdateTransactionResponse> updateTransaction(
            @Name("id") int id,
            @Name("body") UpdateTransactionRequest body) {
        return transactionService.updateTransaction(id, body);
    }

    @Mutation("deleteTransaction")
    @Description("Soft-delete a transaction")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<TrashedTransactionResponse> deleteTransaction(@Name("id") int id) {
        return transactionService.deleteTransaction(id);
    }

    @Mutation("trashedTransaction")
    @Description("Soft-delete a transaction")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<TrashedTransactionResponse> trashedTransaction(@Name("id") int id) {
        return transactionService.deleteTransaction(id);
    }

    @Mutation("restoreTransaction")
    @Description("Restore transaction")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<TrashedTransactionResponse> restoreTransaction(@Name("id") int id) {
        return transactionService.restoreTransaction(id);
    }

    @Mutation("deleteTransactionPermanent")
    @Description("Delete transaction permanently")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteTransactionPermanent(@Name("id") int id) {
        return transactionService.deleteTransactionPermanent(id);
    }

    @Mutation("restoreAllTransactions")
    @Description("Restore all transactions")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> restoreAllTransactions() {
        return transactionService.restoreAllTransactions();
    }

    @Mutation("deleteAllTransactionsPermanent")
    @Description("Delete all transactions permanently")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteAllTransactionsPermanent() {
        return transactionService.deleteAllTransactionsPermanent();
    }

    // STATS
    @Query("getTransactionMonthlyAmountSuccess")
    @Description("Get transaction monthly amount success stats")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<ApiResponseTransactionMonthAmountSuccess> getMonthlyAmountSuccess(
            @Name("year") int year,
            @Name("month") int month) {
        return transactionService.getMonthlyAmountSuccess(year, month);
    }

    @Query("getTransactionYearlyAmountSuccess")
    @Description("Get transaction yearly amount success stats")
    public Uni<ApiResponseTransactionYearAmountSuccess> getYearlyAmountSuccess(
            @Name("year") int year) {
        return transactionService.getYearlyAmountSuccess(year);
    }

    @Query("getTransactionMonthlyAmountFailed")
    @Description("Get transaction monthly amount failed stats")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<ApiResponseTransactionMonthAmountFailed> getMonthlyAmountFailed(
            @Name("year") int year,
            @Name("month") int month) {
        return transactionService.getMonthlyAmountFailed(year, month);
    }

    @Query("getTransactionYearlyAmountFailed")
    @Description("Get transaction yearly amount failed stats")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<ApiResponseTransactionYearAmountFailed> getYearlyAmountFailed(
            @Name("year") int year) {
        return transactionService.getYearlyAmountFailed(year);
    }

    @Query("getTransactionMonthlyMethodSuccess")
    @Description("Get transaction monthly method success stats")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<ApiResponseTransactionMonthPaymentMethod> getMonthlyTransactionMethodSuccess(
            @Name("year") int year,
            @Name("month") int month) {
        return transactionService.getMonthlyTransactionMethodSuccess(year, month);
    }

    @Query("getTransactionYearlyMethodSuccess")
    @Description("Get transaction yearly method success stats")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<ApiResponseTransactionYearPaymentmethod> getYearlyTransactionMethodSuccess(
            @Name("year") int year) {
        return transactionService.getYearlyTransactionMethodSuccess(year);
    }
}
