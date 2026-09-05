package com.sanedge.gateway.dto;

import java.util.List;

public class UserDto {

    @org.eclipse.microprofile.graphql.Name("UserUserResponse")
    public record UserResponse(
            int id,
            String firstname,
            String lastname,
            String email,
            String createdAt,
            String updatedAt) {
        public static UserResponse from(pb.user.UserCommon.UserResponse proto) {
            return new UserResponse(
                    proto.getId(),
                    proto.getFirstname(),
                    proto.getLastname(),
                    proto.getEmail(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
        public static UserResponse from(pb.user.UserCommon.UserResponseDeleteAt proto) {
            return new UserResponse(
                    proto.getId(),
                    proto.getFirstname(),
                    proto.getLastname(),
                    proto.getEmail(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("UserFindAllUserResponse")
    public record FindAllUserResponse(
            List<UserResponse> data,
            String status,
            String message) {
        public static FindAllUserResponse from(pb.user.UserCommon.ApiResponsePaginationUser proto) {
            return new FindAllUserResponse(
                    proto.getDataList().stream().map(UserResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllUserResponse from(pb.user.UserCommon.ApiResponsePaginationUserDeleteAt proto) {
            return new FindAllUserResponse(
                    proto.getDataList().stream().map(UserResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("UserFindByIdUserResponse")
    public record FindByIdUserResponse(
            UserResponse data,
            String status,
            String message) {
        public static FindByIdUserResponse from(pb.user.UserCommon.ApiResponseUser proto) {
            return new FindByIdUserResponse(
                    proto.hasData() ? UserResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindByIdUserResponse from(pb.user.UserCommon.ApiResponseUserDeleteAt proto) {
            return new FindByIdUserResponse(
                    proto.hasData() ? UserResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("UserCreateUserRequest")
    public record CreateUserRequest(
            String firstname,
            String lastname,
            String email,
            String password,
            String confirmPassword) {}

    @org.eclipse.microprofile.graphql.Name("UserCreateUserResponse")
    public record CreateUserResponse(
            UserResponse data,
            String status,
            String message) {
        public static CreateUserResponse from(pb.user.UserCommon.ApiResponseUser proto) {
            return new CreateUserResponse(
                    proto.hasData() ? UserResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("UserUpdateUserRequest")
    public record UpdateUserRequest(
            String firstname,
            String lastname,
            String email,
            String password,
            String confirmPassword) {}

    @org.eclipse.microprofile.graphql.Name("UserUpdateUserResponse")
    public record UpdateUserResponse(
            UserResponse data,
            String status,
            String message) {
        public static UpdateUserResponse from(pb.user.UserCommon.ApiResponseUser proto) {
            return new UpdateUserResponse(
                    proto.hasData() ? UserResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("UserTrashedUserResponse")
    public record TrashedUserResponse(
            UserResponse data,
            String status,
            String message) {
        public static TrashedUserResponse from(pb.user.UserCommon.ApiResponseUserDeleteAt proto) {
            return new TrashedUserResponse(
                    proto.hasData() ? UserResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("UserUpdateUserPasswordRequest")
    public record UpdateUserPasswordRequest(
            String password) {}

    @org.eclipse.microprofile.graphql.Name("UserUpdateUserIsVerifiedRequest")
    public record UpdateUserIsVerifiedRequest(
            boolean isVerified) {}

    @org.eclipse.microprofile.graphql.Name("UserVerifyPasswordRequest")
    public record VerifyPasswordRequest(
            String email,
            String password) {}

    @org.eclipse.microprofile.graphql.Name("UserVerifyPasswordResponse")
    public record VerifyPasswordResponse(
            boolean valid,
            UserResponse user) {
        public static VerifyPasswordResponse from(pb.user.UserCommand.VerifyPasswordResponse proto) {
            return new VerifyPasswordResponse(
                    proto.getValid(),
                    proto.hasUser() ? UserResponse.from(proto.getUser()) : null
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("UserSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.user.UserCommon.ApiResponseUserDelete proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
        public static SimpleStatusMessageResponse from(pb.user.UserCommon.ApiResponseUserAll proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }
}
