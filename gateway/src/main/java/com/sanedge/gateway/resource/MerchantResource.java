package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.MerchantDto.CreateMerchantRequest;
import com.sanedge.gateway.dto.MerchantDto.CreateMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.FindAllMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.FindByIdMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantDto.TrashedMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.UpdateMerchantRequest;
import com.sanedge.gateway.dto.MerchantDto.UpdateMerchantResponse;
import com.sanedge.gateway.service.MerchantService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class MerchantResource {

    @Inject
    MerchantService merchantService;

    @Query("listMerchants")
    @Description("List all merchants")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllMerchantResponse> listMerchants(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantService.listMerchants(page, size, search);
    }

    @Query("getMerchant")
    @Description("Get merchant by ID")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindByIdMerchantResponse> getMerchant(@Name("id") int id) {
        return merchantService.getMerchant(id);
    }

    @Query("listActiveMerchants")
    @Description("List active merchants")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllMerchantResponse> listActiveMerchants(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantService.listActiveMerchants(page, size, search);
    }

    @Query("listTrashedMerchants")
    @Description("List trashed merchants")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindAllMerchantResponse> listTrashedMerchants(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantService.listTrashedMerchants(page, size, search);
    }

    @Mutation("createMerchant")
    @Description("Create a new merchant")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<CreateMerchantResponse> createMerchant(@Name("body") CreateMerchantRequest body) {
        return merchantService.createMerchant(body);
    }

    @Mutation("updateMerchant")
    @Description("Update merchant")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<UpdateMerchantResponse> updateMerchant(@Name("id") int id, @Name("body") UpdateMerchantRequest body) {
        return merchantService.updateMerchant(id, body);
    }

    @Mutation("deleteMerchant")
    @Description("Soft-delete a merchant")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<TrashedMerchantResponse> deleteMerchant(@Name("id") int id) {
        return merchantService.deleteMerchant(id);
    }

    @Mutation("trashedMerchant")
    @Description("Soft-delete a merchant")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<TrashedMerchantResponse> trashedMerchant(@Name("id") int id) {
        return merchantService.deleteMerchant(id);
    }

    @Mutation("restoreMerchant")
    @Description("Restore merchant")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<TrashedMerchantResponse> restoreMerchant(@Name("id") int id) {
        return merchantService.restoreMerchant(id);
    }

    @Mutation("deleteMerchantPermanent")
    @Description("Delete merchant permanently")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteMerchantPermanent(@Name("id") int id) {
        return merchantService.deleteMerchantPermanent(id);
    }

    @Mutation("restoreAllMerchants")
    @Description("Restore all merchants")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> restoreAllMerchants() {
        return merchantService.restoreAllMerchants();
    }

    @Mutation("deleteAllMerchantsPermanent")
    @Description("Delete all merchants permanently")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantsPermanent() {
        return merchantService.deleteAllMerchantsPermanent();
    }
}
