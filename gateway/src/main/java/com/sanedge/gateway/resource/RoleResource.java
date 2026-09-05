package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.RoleDto.CreateRoleRequest;
import com.sanedge.gateway.dto.RoleDto.CreateRoleResponse;
import com.sanedge.gateway.dto.RoleDto.FindAllRoleResponse;
import com.sanedge.gateway.dto.RoleDto.FindByIdRoleResponse;
import com.sanedge.gateway.dto.RoleDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.RoleDto.TrashedRoleResponse;
import com.sanedge.gateway.dto.RoleDto.UpdateRoleRequest;
import com.sanedge.gateway.dto.RoleDto.UpdateRoleResponse;
import com.sanedge.gateway.service.RoleService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class RoleResource {

    @Inject
    RoleService roleService;

    @Query("listRoles")
    @Description("List all roles")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindAllRoleResponse> listRoles(
            @Name("page") @DefaultValue("1") int page,
            @Name("size") @DefaultValue("20") int size,
            @Name("search") String search) {
        return roleService.listRoles(page, size, search);
    }

    @Query("getRole")
    @Description("Get role by ID")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<FindByIdRoleResponse> getRole(@Name("id") int id) {
        return roleService.getRole(id);
    }

    @Mutation("createRole")
    @Description("Create a new role")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<CreateRoleResponse> createRole(@Name("body") CreateRoleRequest body) {
        return roleService.createRole(body);
    }

    @Mutation("updateRole")
    @Description("Update role")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<UpdateRoleResponse> updateRole(@Name("id") int id, @Name("body") UpdateRoleRequest body) {
        return roleService.updateRole(id, body);
    }

    @Mutation("deleteRole")
    @Description("Soft-delete a role")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<TrashedRoleResponse> deleteRole(@Name("id") int id) {
        return roleService.deleteRole(id);
    }

    @Mutation("restoreRole")
    @Description("Restore a role")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<TrashedRoleResponse> restoreRole(@Name("id") int id) {
        return roleService.restoreRole(id);
    }

    @Mutation("deleteRolePermanent")
    @Description("Delete a role permanently")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<SimpleStatusMessageResponse> deleteRolePermanent(@Name("id") int id) {
        return roleService.deleteRolePermanent(id);
    }

    @Mutation("restoreAllRoles")
    @Description("Restore all roles")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> restoreAllRoles() {
        return roleService.restoreAllRoles();
    }

    @Mutation("deleteAllRolesPermanent")
    @Description("Delete all roles permanently")
    @RolesAllowed({ "ROLE_ADMIN" })
    public Uni<SimpleStatusMessageResponse> deleteAllRolesPermanent() {
        return roleService.deleteAllRolesPermanent();
    }
}
