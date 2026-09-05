package com.sanedge.gateway.resource;

import com.sanedge.gateway.dto.MerchantAwardDto.*;
import com.sanedge.gateway.service.MerchantAwardService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class MerchantAwardResource {

    @Inject
    MerchantAwardService merchantAwardService;

    @Query("listMerchantAwards")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllMerchantAwardResponse> listMerchantAwards(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantAwardService.listMerchantAwards(page, size, search);
    }

    @Query("listActiveMerchantAwards")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllMerchantAwardResponse> listActiveMerchantAwards(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantAwardService.listActiveMerchantAwards(page, size, search);
    }

    @Query("listTrashedMerchantAwards")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindAllMerchantAwardResponse> listTrashedMerchantAwards(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantAwardService.listTrashedMerchantAwards(page, size, search);
    }

    @Query("getMerchantAward")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindByIdMerchantAwardResponse> getMerchantAward(@Name("id") int id) {
        return merchantAwardService.getMerchantAward(id);
    }

    @Mutation("createMerchantAward")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<CreateMerchantAwardResponse> createMerchantAward(@Name("body") CreateMerchantAwardRequest body) {
        return merchantAwardService.createMerchantAward(body);
    }

    @Mutation("updateMerchantAward")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<UpdateMerchantAwardResponse> updateMerchantAward(@Name("id") int id, @Name("body") UpdateMerchantAwardRequest body) {
        return merchantAwardService.updateMerchantAward(id, body);
    }

    @Mutation("deleteMerchantAward")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdMerchantAwardResponse> deleteMerchantAward(@Name("id") int id) {
        return merchantAwardService.deleteMerchantAward(id);
    }

    @Mutation("restoreMerchantAward")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdMerchantAwardResponse> restoreMerchantAward(@Name("id") int id) {
        return merchantAwardService.restoreMerchantAward(id);
    }

    @Mutation("deleteMerchantAwardPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteMerchantAwardPermanent(@Name("id") int id) {
        return merchantAwardService.deleteMerchantAwardPermanent(id);
    }

    @Mutation("restoreAllMerchantAwards")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> restoreAllMerchantAwards() {
        return merchantAwardService.restoreAllMerchantAwards();
    }

    @Mutation("deleteAllMerchantAwardsPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantAwardsPermanent() {
        return merchantAwardService.deleteAllMerchantAwardsPermanent();
    }
}
