package com.sanedge.gateway.dto;

import java.util.List;

public class RoleDto {

    @org.eclipse.microprofile.graphql.Name("RoleRoleResponse")
    public record RoleResponse(
            int id,
            String name,
            String createdAt,
            String updatedAt) {
        public static RoleResponse from(pb.role.RoleCommon.RoleResponse proto) {
            return new RoleResponse(proto.getId(), proto.getName(), proto.getCreatedAt(), proto.getUpdatedAt());
        }
        public static RoleResponse from(pb.role.RoleCommon.RoleResponseDeleteAt proto) {
            return new RoleResponse(proto.getId(), proto.getName(), proto.getCreatedAt(), proto.getUpdatedAt());
        }
    }

    @org.eclipse.microprofile.graphql.Name("RoleFindAllRoleResponse")
    public record FindAllRoleResponse(
            List<RoleResponse> data,
            String status,
            String message) {
        public static FindAllRoleResponse from(pb.role.RoleCommon.ApiResponsePaginationRole proto) {
            return new FindAllRoleResponse(
                    proto.getDataList().stream().map(RoleResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllRoleResponse from(pb.role.RoleCommon.ApiResponsePaginationRoleDeleteAt proto) {
            return new FindAllRoleResponse(
                    proto.getDataList().stream().map(RoleResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllRoleResponse from(pb.role.RoleCommon.ApiResponsesRole proto) {
            return new FindAllRoleResponse(
                    proto.getDataList().stream().map(RoleResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("RoleFindByIdRoleResponse")
    public record FindByIdRoleResponse(
            RoleResponse data,
            String status,
            String message) {
        public static FindByIdRoleResponse from(pb.role.RoleCommon.ApiResponseRole proto) {
            return new FindByIdRoleResponse(
                    proto.hasData() ? RoleResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindByIdRoleResponse from(pb.role.RoleCommon.ApiResponseRoleDeleteAt proto) {
            return new FindByIdRoleResponse(
                    proto.hasData() ? RoleResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("RoleCreateRoleRequest")
    public record CreateRoleRequest(
            String name) {}

    @org.eclipse.microprofile.graphql.Name("RoleCreateRoleResponse")
    public record CreateRoleResponse(
            RoleResponse data,
            String status,
            String message) {
        public static CreateRoleResponse from(pb.role.RoleCommon.ApiResponseRole proto) {
            return new CreateRoleResponse(
                    proto.hasData() ? RoleResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("RoleUpdateRoleRequest")
    public record UpdateRoleRequest(
            String name) {}

    @org.eclipse.microprofile.graphql.Name("RoleUpdateRoleResponse")
    public record UpdateRoleResponse(
            RoleResponse data,
            String status,
            String message) {
        public static UpdateRoleResponse from(pb.role.RoleCommon.ApiResponseRole proto) {
            return new UpdateRoleResponse(
                    proto.hasData() ? RoleResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("RoleTrashedRoleResponse")
    public record TrashedRoleResponse(
            RoleResponse data,
            String status,
            String message) {
        public static TrashedRoleResponse from(pb.role.RoleCommon.ApiResponseRoleDeleteAt proto) {
            return new TrashedRoleResponse(
                    proto.hasData() ? RoleResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("RoleSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.role.RoleCommon.ApiResponseRoleDelete proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
        public static SimpleStatusMessageResponse from(pb.role.RoleCommon.ApiResponseRoleAll proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }

    @org.eclipse.microprofile.graphql.Name("RoleUserRoleResponse")
    public record UserRoleResponse(
            int userRoleId,
            int userId,
            int roleId,
            String createdAt,
            String updatedAt) {
        public static UserRoleResponse from(pb.role.RoleCommon.UserRoleResponse proto) {
            return new UserRoleResponse(
                    proto.getUserRoleId(),
                    proto.getUserId(),
                    proto.getRoleId(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("RoleAssignRoleToUserResponse")
    public record AssignRoleToUserResponse(
            String status,
            String message,
            UserRoleResponse data) {
        public static AssignRoleToUserResponse from(pb.role.RoleCommon.ApiResponseUserRole proto) {
            return new AssignRoleToUserResponse(
                    proto.getStatus(),
                    proto.getMessage(),
                    proto.hasData() ? UserRoleResponse.from(proto.getData()) : null
            );
        }
    }
}
