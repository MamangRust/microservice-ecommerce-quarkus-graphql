package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.UserDto.CreateUserRequest;
import com.sanedge.gateway.dto.UserDto.CreateUserResponse;
import com.sanedge.gateway.dto.UserDto.FindAllUserResponse;
import com.sanedge.gateway.dto.UserDto.FindByIdUserResponse;
import com.sanedge.gateway.dto.UserDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.UserDto.TrashedUserResponse;
import com.sanedge.gateway.dto.UserDto.UpdateUserRequest;
import com.sanedge.gateway.dto.UserDto.UpdateUserResponse;
import com.sanedge.gateway.service.UserService;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class UserResource {

    @Inject
    UserService userService;

    @Query("listUsers")
    @Description("List all users")
    public Uni<FindAllUserResponse> listUsers(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return userService.listUsers(page, size, search);
    }

    @Query("getUser")
    @Description("Get user by ID")
    public Uni<FindByIdUserResponse> getUser(@Name("id") int id) {
        return userService.getUser(id);
    }

    @Query("listActiveUsers")
    @Description("List active users")
    public Uni<FindAllUserResponse> listActiveUsers(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return userService.listActiveUsers(page, size, search);
    }

    @Query("listTrashedUsers")
    @Description("List trashed users")
    public Uni<FindAllUserResponse> listTrashedUsers(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return userService.listTrashedUsers(page, size, search);
    }

    @Mutation("createUser")
    @Description("Create a new user")
    public Uni<CreateUserResponse> createUser(@Name("body") CreateUserRequest body) {
        return userService.createUser(body);
    }

    @Mutation("updateUser")
    @Description("Update user")
    public Uni<UpdateUserResponse> updateUser(@Name("id") int id, @Name("body") UpdateUserRequest body) {
        return userService.updateUser(id, body);
    }

    @Mutation("deleteUser")
    @Description("Soft-delete a user")
    public Uni<TrashedUserResponse> deleteUser(@Name("id") int id) {
        return userService.deleteUser(id);
    }

    @Mutation("trashedUser")
    @Description("Soft-delete a user")
    public Uni<TrashedUserResponse> trashedUser(@Name("id") int id) {
        return userService.deleteUser(id);
    }

    @Mutation("restoreUser")
    @Description("Restore user")
    public Uni<TrashedUserResponse> restoreUser(@Name("id") int id) {
        return userService.restoreUser(id);
    }

    @Mutation("deleteUserPermanent")
    @Description("Delete user permanently")
    public Uni<SimpleStatusMessageResponse> deleteUserPermanent(@Name("id") int id) {
        return userService.deleteUserPermanent(id);
    }

    @Mutation("restoreAllUsers")
    @Description("Restore all users")
    public Uni<SimpleStatusMessageResponse> restoreAllUsers() {
        return userService.restoreAllUsers();
    }

    @Mutation("deleteAllUsersPermanent")
    @Description("Delete all users permanently")
    public Uni<SimpleStatusMessageResponse> deleteAllUsersPermanent() {
        return userService.deleteAllUsersPermanent();
    }
}