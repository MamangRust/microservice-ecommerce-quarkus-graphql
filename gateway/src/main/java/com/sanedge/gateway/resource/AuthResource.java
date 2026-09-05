package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.sanedge.gateway.dto.AuthDto;
import com.sanedge.gateway.service.AuthService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class AuthResource {

    @Inject
    AuthService authService;

    @Mutation("register")
    @Description("Register a new user")
    @PermitAll
    public Uni<AuthDto.RegisterResponse> register(@Name("body") AuthDto.RegisterRequest body) {
        return authService.register(body);
    }

    @Mutation("login")
    @Description("Login a user")
    @PermitAll
    public Uni<AuthDto.LoginResponse> login(@Name("body") AuthDto.LoginRequest body) {
        return authService.login(body);
    }

    @Mutation("verify")
    @Description("Verify user email by verification code")
    @PermitAll
    public Uni<AuthDto.SimpleResponse> verify(@Name("body") AuthDto.VerifyCodeRequest body) {
        return authService.verify(body);
    }

    @Mutation("forgotPassword")
    @Description("Initiate forgot password request")
    @PermitAll
    public Uni<AuthDto.SimpleResponse> forgotPassword(@Name("body") AuthDto.ForgotPasswordRequest body) {
        return authService.forgotPassword(body);
    }

    @Mutation("resetPassword")
    @Description("Reset user password")
    @PermitAll
    public Uni<AuthDto.SimpleResponse> resetPassword(@Name("body") AuthDto.ResetPasswordRequest body) {
        return authService.resetPassword(body);
    }

    @Mutation("refresh")
    @Description("Refresh user access token")
    @PermitAll
    public Uni<AuthDto.RefreshTokenResponse> refresh(@Name("body") AuthDto.RefreshTokenRequest body) {
        return authService.refresh(body);
    }

    @Query("getMe")
    @Description("Get current logged-in user profile")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
    public Uni<AuthDto.GetMeResponse> getMe(@Name("userId") int userId) {
        return authService.getMe(userId);
    }
}

