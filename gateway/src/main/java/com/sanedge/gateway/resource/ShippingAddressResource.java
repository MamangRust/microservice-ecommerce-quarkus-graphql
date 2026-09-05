package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.ShippingAddressDto.CreateShippingAddressRequest;
import com.sanedge.gateway.dto.ShippingAddressDto.CreateShippingAddressResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.FindAllShippingResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.FindByIdShippingResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.UpdateShippingAddressRequest;
import com.sanedge.gateway.dto.ShippingAddressDto.UpdateShippingAddressResponse;
import com.sanedge.gateway.service.ShippingAddressService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class ShippingAddressResource {

        @Inject
        ShippingAddressService shippingAddressService;

        @Query("listShippingAddresses")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllShippingResponse> listShippingAddresses(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return shippingAddressService.listShippingAddresses(page, size, search);
        }

        @Query("getShippingAddressByOrder")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindByIdShippingResponse> getShippingAddressByOrder(@Name("orderId") int orderId) {
                return shippingAddressService.getShippingAddressByOrder(orderId);
        }

        @Query("listActiveShippingAddresses")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindAllShippingResponse> listActiveShippingAddresses(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return shippingAddressService.listActiveShippingAddresses(page, size, search);
        }

        @Query("listTrashedShippingAddresses")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindAllShippingResponse> listTrashedShippingAddresses(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return shippingAddressService.listTrashedShippingAddresses(page, size, search);
        }

        @Query("getShippingAddress")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindByIdShippingResponse> getShippingAddress(@Name("id") int id) {
                return shippingAddressService.getShippingAddress(id);
        }

        @Mutation("createShippingAddress")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<CreateShippingAddressResponse> createShippingAddress(
                        @Name("body") CreateShippingAddressRequest body) {
                return shippingAddressService.createShippingAddress(body);
        }

        @Mutation("updateShippingAddress")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<UpdateShippingAddressResponse> updateShippingAddress(@Name("id") int id,
                        @Name("body") UpdateShippingAddressRequest body) {
                return shippingAddressService.updateShippingAddress(id, body);
        }

        @Mutation("deleteShippingAddress")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindByIdShippingResponse> deleteShippingAddress(@Name("id") int id) {
                return shippingAddressService.deleteShippingAddress(id);
        }

        @Mutation("restoreShippingAddress")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindByIdShippingResponse> restoreShippingAddress(@Name("id") int id) {
                return shippingAddressService.restoreShippingAddress(id);
        }

        @Mutation("deleteShippingAddressPermanent")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteShippingAddressPermanent(@Name("id") int id) {
                return shippingAddressService.deleteShippingAddressPermanent(id);
        }

        @Mutation("restoreAllShippingAddresses")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> restoreAllShippingAddresses() {
                return shippingAddressService.restoreAllShippingAddresses();
        }

        @Mutation("deleteAllShippingAddressesPermanent")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteAllShippingAddressesPermanent() {
                return shippingAddressService.deleteAllShippingAddressesPermanent();
        }
}
