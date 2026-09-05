package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.UserDto.CreateUserRequest;
import com.sanedge.gateway.dto.UserDto.CreateUserResponse;
import com.sanedge.gateway.dto.UserDto.FindAllUserResponse;
import com.sanedge.gateway.dto.UserDto.FindByIdUserResponse;
import com.sanedge.gateway.dto.UserDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.UserDto.TrashedUserResponse;
import com.sanedge.gateway.dto.UserDto.UpdateUserRequest;
import com.sanedge.gateway.dto.UserDto.UpdateUserResponse;
import io.smallrye.mutiny.Uni;

public interface UserService {
    Uni<FindAllUserResponse> listUsers(int page, int size, String search);
    Uni<FindByIdUserResponse> getUser(int id);
    Uni<FindAllUserResponse> listActiveUsers(int page, int size, String search);
    Uni<FindAllUserResponse> listTrashedUsers(int page, int size, String search);
    Uni<CreateUserResponse> createUser(CreateUserRequest body);
    Uni<UpdateUserResponse> updateUser(int id, UpdateUserRequest body);
    Uni<TrashedUserResponse> deleteUser(int id);
    Uni<TrashedUserResponse> restoreUser(int id);
    Uni<SimpleStatusMessageResponse> deleteUserPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllUsers();
    Uni<SimpleStatusMessageResponse> deleteAllUsersPermanent();
}
