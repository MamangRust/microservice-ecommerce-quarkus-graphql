package com.sanedge.gateway.resource;

import com.sanedge.gateway.dto.MerchantBusinessDto.*;
import com.sanedge.gateway.service.MerchantBusinessService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class MerchantBusinessResource {

    @Inject
    MerchantBusinessService merchantBusinessService;

    @Query("listMerchantBusinesses")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllMerchantBusinessResponse> listMerchantBusinesses(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantBusinessService.listMerchantBusinesses(page, size, search);
    }

    @Query("listActiveMerchantBusinesses")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllMerchantBusinessResponse> listActiveMerchantBusinesses(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantBusinessService.listActiveMerchantBusinesses(page, size, search);
    }

    @Query("listTrashedMerchantBusinesses")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindAllMerchantBusinessResponse> listTrashedMerchantBusinesses(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantBusinessService.listTrashedMerchantBusinesses(page, size, search);
    }

    @Query("getMerchantBusiness")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindByIdMerchantBusinessResponse> getMerchantBusiness(@Name("id") int id) {
        return merchantBusinessService.getMerchantBusiness(id);
    }

    @Mutation("createMerchantBusiness")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<CreateMerchantBusinessResponse> createMerchantBusiness(@Name("body") CreateMerchantBusinessRequest body) {
        return merchantBusinessService.createMerchantBusiness(body);
    }

    @Mutation("updateMerchantBusiness")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<UpdateMerchantBusinessResponse> updateMerchantBusiness(@Name("id") int id, @Name("body") UpdateMerchantBusinessRequest body) {
        return merchantBusinessService.updateMerchantBusiness(id, body);
    }

    @Mutation("deleteMerchantBusiness")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdMerchantBusinessResponse> deleteMerchantBusiness(@Name("id") int id) {
        return merchantBusinessService.deleteMerchantBusiness(id);
    }

    @Mutation("restoreMerchantBusiness")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdMerchantBusinessResponse> restoreMerchantBusiness(@Name("id") int id) {
        return merchantBusinessService.restoreMerchantBusiness(id);
    }

    @Mutation("deleteMerchantBusinessPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteMerchantBusinessPermanent(@Name("id") int id) {
        return merchantBusinessService.deleteMerchantBusinessPermanent(id);
    }

    @Mutation("restoreAllMerchantBusinesses")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> restoreAllMerchantBusinesses() {
        return merchantBusinessService.restoreAllMerchantBusinesses();
    }

    @Mutation("deleteAllMerchantBusinessesPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantBusinessesPermanent() {
        return merchantBusinessService.deleteAllMerchantBusinessesPermanent();
    }
}
