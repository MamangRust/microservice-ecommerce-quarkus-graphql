package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.RoleDto.CreateRoleRequest;
import com.sanedge.gateway.dto.RoleDto.CreateRoleResponse;
import com.sanedge.gateway.dto.RoleDto.FindAllRoleResponse;
import com.sanedge.gateway.dto.RoleDto.FindByIdRoleResponse;
import com.sanedge.gateway.dto.RoleDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.RoleDto.TrashedRoleResponse;
import com.sanedge.gateway.dto.RoleDto.UpdateRoleRequest;
import com.sanedge.gateway.dto.RoleDto.UpdateRoleResponse;
import io.smallrye.mutiny.Uni;

public interface RoleService {
    Uni<FindAllRoleResponse> listRoles(int page, int size, String search);

    Uni<FindByIdRoleResponse> getRole(int id);

    Uni<CreateRoleResponse> createRole(CreateRoleRequest body);

    Uni<UpdateRoleResponse> updateRole(int id, UpdateRoleRequest body);

    Uni<TrashedRoleResponse> deleteRole(int id);

    Uni<TrashedRoleResponse> restoreRole(int id);

    Uni<SimpleStatusMessageResponse> deleteRolePermanent(int id);

    Uni<SimpleStatusMessageResponse> restoreAllRoles();

    Uni<SimpleStatusMessageResponse> deleteAllRolesPermanent();
}
