package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.RoleDto.CreateRoleRequest;
import com.sanedge.gateway.dto.RoleDto.CreateRoleResponse;
import com.sanedge.gateway.dto.RoleDto.FindAllRoleResponse;
import com.sanedge.gateway.dto.RoleDto.FindByIdRoleResponse;
import com.sanedge.gateway.dto.RoleDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.RoleDto.TrashedRoleResponse;
import com.sanedge.gateway.dto.RoleDto.UpdateRoleRequest;
import com.sanedge.gateway.dto.RoleDto.UpdateRoleResponse;
import com.sanedge.gateway.service.RoleService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RoleServiceImpl implements RoleService {

        @Inject
        TelemetryHelper telemetryHelper;

        @GrpcClient("role")
        pb.role.MutinyRoleQueryServiceGrpc.MutinyRoleQueryServiceStub roleQueryService;

        @GrpcClient("role")
        pb.role.MutinyRoleCommandServiceGrpc.MutinyRoleCommandServiceStub roleCommandService;

        @Override
        public Uni<FindAllRoleResponse> listRoles(int page, int size, String search) {
                return telemetryHelper.traceAndMetric("role.listRoles",
                                () -> roleQueryService.findAllRole(pb.role.RoleQuery.FindAllRoleRequest.newBuilder()
                                                .setPage(page)
                                                .setPageSize(size)
                                                .setSearch(search == null ? "" : search)
                                                .build())
                                                .map(FindAllRoleResponse::from));
        }

        @Override
        public Uni<FindByIdRoleResponse> getRole(int id) {
                return telemetryHelper.traceAndMetric("role.getRole",
                                () -> roleQueryService.findByIdRole(pb.role.RoleCommon.FindByIdRoleRequest.newBuilder()
                                                .setRoleId(id)
                                                .build())
                                                .map(FindByIdRoleResponse::from));
        }

        @Override
        public Uni<CreateRoleResponse> createRole(CreateRoleRequest body) {
                return telemetryHelper.traceAndMetric("role.createRole",
                                () -> roleCommandService.createRole(pb.role.RoleCommand.CreateRoleRequest.newBuilder()
                                                .setName(body.name() == null ? "" : body.name())
                                                .build())
                                                .map(CreateRoleResponse::from));
        }

        @Override
        public Uni<UpdateRoleResponse> updateRole(int id, UpdateRoleRequest body) {
                return telemetryHelper.traceAndMetric("role.updateRole",
                                () -> roleCommandService.updateRole(pb.role.RoleCommand.UpdateRoleRequest.newBuilder()
                                                .setId(id)
                                                .setName(body.name() == null ? "" : body.name())
                                                .build())
                                                .map(UpdateRoleResponse::from));
        }

        @Override
        public Uni<TrashedRoleResponse> deleteRole(int id) {
                return telemetryHelper.traceAndMetric("role.deleteRole",
                                () -> roleCommandService.trashedRole(pb.role.RoleCommon.FindByIdRoleRequest.newBuilder()
                                                .setRoleId(id)
                                                .build())
                                                .map(TrashedRoleResponse::from));
        }

        @Override
        public Uni<TrashedRoleResponse> restoreRole(int id) {
                return telemetryHelper.traceAndMetric("role.restoreRole",
                                () -> roleCommandService.restoreRole(pb.role.RoleCommon.FindByIdRoleRequest.newBuilder()
                                                .setRoleId(id)
                                                .build())
                                                .map(TrashedRoleResponse::from));
        }

        @Override
        public Uni<SimpleStatusMessageResponse> deleteRolePermanent(int id) {
                return telemetryHelper.traceAndMetric("role.deleteRolePermanent",
                                () -> roleCommandService
                                                .deleteRolePermanent(pb.role.RoleCommon.FindByIdRoleRequest.newBuilder()
                                                                .setRoleId(id)
                                                                .build())
                                                .map(SimpleStatusMessageResponse::from));
        }

        @Override
        public Uni<SimpleStatusMessageResponse> restoreAllRoles() {
                return telemetryHelper.traceAndMetric("role.restoreAllRoles",
                                () -> roleCommandService
                                                .restoreAllRole(com.google.protobuf.Empty.getDefaultInstance())
                                                .map(SimpleStatusMessageResponse::from));
        }

        @Override
        public Uni<SimpleStatusMessageResponse> deleteAllRolesPermanent() {
                return telemetryHelper.traceAndMetric("role.deleteAllRolesPermanent",
                                () -> roleCommandService
                                                .deleteAllRolePermanent(com.google.protobuf.Empty.getDefaultInstance())
                                                .map(SimpleStatusMessageResponse::from));
        }
}

