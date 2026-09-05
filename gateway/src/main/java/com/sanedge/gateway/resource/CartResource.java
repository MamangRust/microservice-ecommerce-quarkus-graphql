package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.CartDto.CreateCartRequest;
import com.sanedge.gateway.dto.CartDto.CreateCartResponse;
import com.sanedge.gateway.dto.CartDto.FindAllCartResponse;
import com.sanedge.gateway.dto.CartDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.service.CartService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

@GraphQLApi
@Singleton
public class CartResource {

    @Inject
    CartService cartService;

    @Query("listCarts")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllCartResponse> listCarts(
            @Name("userId") int userId,
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return cartService.listCarts(userId, page, size, search);
    }

    @Mutation("createCart")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<CreateCartResponse> createCart(@Name("body") CreateCartRequest body) {
        return cartService.createCart(body);
    }

    @Mutation("deleteCart")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<SimpleStatusMessageResponse> deleteCart(
            @Name("cartId") int cartId,
            @Name("userId") int userId) {
        return cartService.deleteCart(cartId, userId);
    }

    @Mutation("deleteAllCarts")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<SimpleStatusMessageResponse> deleteAllCarts(
            @Name("userId") int userId,
            @Name("cartIds") List<Integer> cartIds) {
        return cartService.deleteAllCarts(userId, cartIds);
    }
}
