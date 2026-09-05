package com.sanedge.gateway.resource;

import com.sanedge.gateway.dto.MerchantDetailDto.*;
import com.sanedge.gateway.service.MerchantDetailService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class MerchantDetailResource {

    @Inject
    MerchantDetailService merchantDetailService;

    @Query("listMerchantDetails")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllMerchantDetailResponse> listMerchantDetails(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantDetailService.listMerchantDetails(page, size, search);
    }

    @Query("listActiveMerchantDetails")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllMerchantDetailResponse> listActiveMerchantDetails(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantDetailService.listActiveMerchantDetails(page, size, search);
    }

    @Query("listTrashedMerchantDetails")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindAllMerchantDetailResponse> listTrashedMerchantDetails(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return merchantDetailService.listTrashedMerchantDetails(page, size, search);
    }

    @Query("getMerchantDetail")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindByIdMerchantDetailResponse> getMerchantDetail(@Name("id") int id) {
        return merchantDetailService.getMerchantDetail(id);
    }

    @Mutation("createMerchantDetail")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<CreateMerchantDetailResponse> createMerchantDetail(@Name("body") CreateMerchantDetailRequest body) {
        return merchantDetailService.createMerchantDetail(body);
    }

    @Mutation("updateMerchantDetail")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<UpdateMerchantDetailResponse> updateMerchantDetail(@Name("id") int id, @Name("body") UpdateMerchantDetailRequest body) {
        return merchantDetailService.updateMerchantDetail(id, body);
    }

    @Mutation("deleteMerchantDetail")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdMerchantDetailResponse> deleteMerchantDetail(@Name("id") int id) {
        return merchantDetailService.deleteMerchantDetail(id);
    }

    @Mutation("restoreMerchantDetail")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdMerchantDetailResponse> restoreMerchantDetail(@Name("id") int id) {
        return merchantDetailService.restoreMerchantDetail(id);
    }

    @Mutation("deleteMerchantDetailPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteMerchantDetailPermanent(@Name("id") int id) {
        return merchantDetailService.deleteMerchantDetailPermanent(id);
    }

    @Mutation("restoreAllMerchantDetails")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> restoreAllMerchantDetails() {
        return merchantDetailService.restoreAllMerchantDetails();
    }

    @Mutation("deleteAllMerchantDetailsPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantDetailsPermanent() {
        return merchantDetailService.deleteAllMerchantDetailsPermanent();
    }
}
