package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.UserDto.CreateUserRequest;
import com.sanedge.gateway.dto.UserDto.CreateUserResponse;
import com.sanedge.gateway.dto.UserDto.FindAllUserResponse;
import com.sanedge.gateway.dto.UserDto.FindByIdUserResponse;
import com.sanedge.gateway.dto.UserDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.UserDto.TrashedUserResponse;
import com.sanedge.gateway.dto.UserDto.UpdateUserRequest;
import com.sanedge.gateway.dto.UserDto.UpdateUserResponse;
import com.sanedge.gateway.service.UserService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("user")
    pb.user.MutinyUserQueryServiceGrpc.MutinyUserQueryServiceStub userQueryService;

    @GrpcClient("user")
    pb.user.MutinyUserCommandServiceGrpc.MutinyUserCommandServiceStub userCommandService;

    @Override
    public Uni<FindAllUserResponse> listUsers(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("user.listUsers", () -> userQueryService.findAll(pb.user.UserQuery.FindAllUserRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllUserResponse::from));
    }

    @Override
    public Uni<FindByIdUserResponse> getUser(int id) {
        return telemetryHelper.traceAndMetric("user.getUser", () -> userQueryService.findById(pb.user.UserCommon.FindByIdUserRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdUserResponse::from));
    }

    @Override
    public Uni<FindAllUserResponse> listActiveUsers(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("user.listActiveUsers", () -> userQueryService.findByActive(pb.user.UserQuery.FindAllUserRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllUserResponse::from));
    }

    @Override
    public Uni<FindAllUserResponse> listTrashedUsers(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("user.listTrashedUsers", () -> userQueryService.findByTrashed(pb.user.UserQuery.FindAllUserRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllUserResponse::from));
    }

    @Override
    public Uni<CreateUserResponse> createUser(CreateUserRequest body) {
        return telemetryHelper.traceAndMetric("user.createUser", () -> userCommandService.create(pb.user.UserCommand.CreateUserRequest.newBuilder()
                .setFirstname(body.firstname() == null ? "" : body.firstname())
                .setLastname(body.lastname() == null ? "" : body.lastname())
                .setEmail(body.email() == null ? "" : body.email())
                .setPassword(body.password() == null ? "" : body.password())
                .setConfirmPassword(body.confirmPassword() == null ? "" : body.confirmPassword())
                .build())
                .map(CreateUserResponse::from));
    }

    @Override
    public Uni<UpdateUserResponse> updateUser(int id, UpdateUserRequest body) {
        return telemetryHelper.traceAndMetric("user.updateUser", () -> userCommandService.update(pb.user.UserCommand.UpdateUserRequest.newBuilder()
                .setId(id)
                .setFirstname(body.firstname() == null ? "" : body.firstname())
                .setLastname(body.lastname() == null ? "" : body.lastname())
                .setEmail(body.email() == null ? "" : body.email())
                .setPassword(body.password() == null ? "" : body.password())
                .setConfirmPassword(body.confirmPassword() == null ? "" : body.confirmPassword())
                .build())
                .map(UpdateUserResponse::from));
    }

    @Override
    public Uni<TrashedUserResponse> deleteUser(int id) {
        return telemetryHelper.traceAndMetric("user.deleteUser", () -> userCommandService.trashedUser(pb.user.UserCommon.FindByIdUserRequest.newBuilder()
                .setId(id)
                .build())
                .map(TrashedUserResponse::from));
    }

    @Override
    public Uni<TrashedUserResponse> restoreUser(int id) {
        return telemetryHelper.traceAndMetric("user.restoreUser", () -> userCommandService.restoreUser(pb.user.UserCommon.FindByIdUserRequest.newBuilder()
                .setId(id)
                .build())
                .map(TrashedUserResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteUserPermanent(int id) {
        return telemetryHelper.traceAndMetric("user.deleteUserPermanent", () -> userCommandService.deleteUserPermanent(pb.user.UserCommon.FindByIdUserRequest.newBuilder()
                .setId(id)
                .build())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllUsers() {
        return telemetryHelper.traceAndMetric("user.restoreAllUsers", () -> userCommandService.restoreAllUser(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllUsersPermanent() {
        return telemetryHelper.traceAndMetric("user.deleteAllUsersPermanent", () -> userCommandService.deleteAllUserPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }
}
