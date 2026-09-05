package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.BannerDto.CreateBannerRequest;
import com.sanedge.gateway.dto.BannerDto.CreateBannerResponse;
import com.sanedge.gateway.dto.BannerDto.FindAllBannerResponse;
import com.sanedge.gateway.dto.BannerDto.FindByIdBannerResponse;
import com.sanedge.gateway.dto.BannerDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.BannerDto.UpdateBannerRequest;
import com.sanedge.gateway.dto.BannerDto.UpdateBannerResponse;
import com.sanedge.gateway.service.BannerService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class BannerResource {

    @Inject
    BannerService bannerService;

    @Query("listBanners")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllBannerResponse> listBanners(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return bannerService.listBanners(page, size, search);
    }

    @Query("listActiveBanners")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllBannerResponse> listActiveBanners(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return bannerService.listActiveBanners(page, size, search);
    }

    @Query("listTrashedBanners")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindAllBannerResponse> listTrashedBanners(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return bannerService.listTrashedBanners(page, size, search);
    }

    @Query("getBanner")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindByIdBannerResponse> getBanner(@Name("id") int id) {
        return bannerService.getBanner(id);
    }

    @Mutation("createBanner")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<CreateBannerResponse> createBanner(@Name("body") CreateBannerRequest body) {
        return bannerService.createBanner(body);
    }

    @Mutation("updateBanner")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<UpdateBannerResponse> updateBanner(@Name("id") int id, @Name("body") UpdateBannerRequest body) {
        return bannerService.updateBanner(id, body);
    }

    @Mutation("deleteBanner")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
    public Uni<FindByIdBannerResponse> deleteBanner(@Name("id") int id) {
        return bannerService.deleteBanner(id);
    }

    @Mutation("restoreBanner")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<FindByIdBannerResponse> restoreBanner(@Name("id") int id) {
        return bannerService.restoreBanner(id);
    }

    @Mutation("deleteBannerPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteBannerPermanent(@Name("id") int id) {
        return bannerService.deleteBannerPermanent(id);
    }

    @Mutation("restoreAllBanners")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> restoreAllBanners() {
        return bannerService.restoreAllBanners();
    }

    @Mutation("deleteAllBannersPermanent")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteAllBannersPermanent() {
        return bannerService.deleteAllBannersPermanent();
    }
}
