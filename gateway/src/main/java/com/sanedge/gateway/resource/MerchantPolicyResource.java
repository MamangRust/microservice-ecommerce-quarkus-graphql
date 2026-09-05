package com.sanedge.gateway.resource;

import com.sanedge.gateway.dto.MerchantPolicyDto.*;
import com.sanedge.gateway.service.MerchantPolicyService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class MerchantPolicyResource {

    @Inject
    MerchantPolicyService merchantPolicyService;

    @Query("listMerchantPolicies")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllMerchantPolicyResponse> listMerchantPolicies(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantPolicyService.listMerchantPolicies(page, size, search);
    }

    @Query("listActiveMerchantPolicies")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllMerchantPolicyResponse> listActiveMerchantPolicies(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantPolicyService.listActiveMerchantPolicies(page, size, search);
    }

    @Query("listTrashedMerchantPolicies")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindAllMerchantPolicyResponse> listTrashedMerchantPolicies(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantPolicyService.listTrashedMerchantPolicies(page, size, search);
    }

    @Query("getMerchantPolicy")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindByIdMerchantPolicyResponse> getMerchantPolicy(@Name("id") int id) {
        return merchantPolicyService.getMerchantPolicy(id);
    }

    @Mutation("createMerchantPolicy")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<CreateMerchantPolicyResponse> createMerchantPolicy(@Name("body") CreateMerchantPolicyRequest body) {
        return merchantPolicyService.createMerchantPolicy(body);
    }

    @Mutation("updateMerchantPolicy")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<UpdateMerchantPolicyResponse> updateMerchantPolicy(@Name("id") int id, @Name("body") UpdateMerchantPolicyRequest body) {
        return merchantPolicyService.updateMerchantPolicy(id, body);
    }

    @Mutation("deleteMerchantPolicy")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdMerchantPolicyResponse> deleteMerchantPolicy(@Name("id") int id) {
        return merchantPolicyService.deleteMerchantPolicy(id);
    }

    @Mutation("restoreMerchantPolicy")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdMerchantPolicyResponse> restoreMerchantPolicy(@Name("id") int id) {
        return merchantPolicyService.restoreMerchantPolicy(id);
    }

    @Mutation("deleteMerchantPolicyPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteMerchantPolicyPermanent(@Name("id") int id) {
        return merchantPolicyService.deleteMerchantPolicyPermanent(id);
    }

    @Mutation("restoreAllMerchantPolicies")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> restoreAllMerchantPolicies() {
        return merchantPolicyService.restoreAllMerchantPolicies();
    }

    @Mutation("deleteAllMerchantPoliciesPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantPoliciesPermanent() {
        return merchantPolicyService.deleteAllMerchantPoliciesPermanent();
    }
}
