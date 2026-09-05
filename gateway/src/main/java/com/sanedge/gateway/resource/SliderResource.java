package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.sanedge.gateway.dto.SliderDto.CreateSliderRequest;
import com.sanedge.gateway.dto.SliderDto.CreateSliderResponse;
import com.sanedge.gateway.dto.SliderDto.FindAllSliderResponse;
import com.sanedge.gateway.dto.SliderDto.FindByIdSliderResponse;
import com.sanedge.gateway.dto.SliderDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.SliderDto.UpdateSliderRequest;
import com.sanedge.gateway.dto.SliderDto.UpdateSliderResponse;
import com.sanedge.gateway.service.SliderService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class SliderResource {

        @Inject
        SliderService sliderService;

        @Query("listSliders")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllSliderResponse> listSliders(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return sliderService.listSliders(page, size, search);
        }

        @Query("listActiveSliders")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindAllSliderResponse> listActiveSliders(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return sliderService.listActiveSliders(page, size, search);
        }

        @Query("listTrashedSliders")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindAllSliderResponse> listTrashedSliders(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return sliderService.listTrashedSliders(page, size, search);
        }

        @Query("getSlider")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindByIdSliderResponse> getSlider(@Name("id") int id) {
                return sliderService.getSlider(id);
        }

        @Mutation("createSlider")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CreateSliderResponse> createSlider(@Name("body") CreateSliderRequest body) {
                return sliderService.createSlider(body);
        }

        @Mutation("updateSlider")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<UpdateSliderResponse> updateSlider(@Name("id") int id, @Name("body") UpdateSliderRequest body) {
                return sliderService.updateSlider(id, body);
        }

        @Mutation("uploadSlider")
        @Description("Upload slider image")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<UpdateSliderResponse> uploadSlider(
                        @Name("id") int id,
                        @Name("file") FileUpload file) {
                return sliderService.uploadSlider(id, file);
        }

        @Mutation("deleteSlider")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<FindByIdSliderResponse> deleteSlider(@Name("id") int id) {
                return sliderService.deleteSlider(id);
        }

        @Mutation("restoreSlider")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<FindByIdSliderResponse> restoreSlider(@Name("id") int id) {
                return sliderService.restoreSlider(id);
        }

        @Mutation("deleteSliderPermanent")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteSliderPermanent(@Name("id") int id) {
                return sliderService.deleteSliderPermanent(id);
        }

        @Mutation("restoreAllSliders")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> restoreAllSliders() {
                return sliderService.restoreAllSliders();
        }

        @Mutation("deleteAllSlidersPermanent")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteAllSlidersPermanent() {
                return sliderService.deleteAllSlidersPermanent();
        }
}
