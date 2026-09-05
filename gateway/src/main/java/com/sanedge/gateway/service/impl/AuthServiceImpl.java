package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.AuthDto.RegisterRequest;
import com.sanedge.gateway.dto.AuthDto.RegisterResponse;
import com.sanedge.gateway.dto.AuthDto.LoginRequest;
import com.sanedge.gateway.dto.AuthDto.LoginResponse;
import com.sanedge.gateway.dto.AuthDto.VerifyCodeRequest;
import com.sanedge.gateway.dto.AuthDto.SimpleResponse;
import com.sanedge.gateway.dto.AuthDto.ForgotPasswordRequest;
import com.sanedge.gateway.dto.AuthDto.ResetPasswordRequest;
import com.sanedge.gateway.dto.AuthDto.RefreshTokenRequest;
import com.sanedge.gateway.dto.AuthDto.RefreshTokenResponse;
import com.sanedge.gateway.dto.AuthDto.GetMeResponse;
import com.sanedge.gateway.service.AuthService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("auth")
    pb.MutinyAuthServiceGrpc.MutinyAuthServiceStub authService;

    @Override
    public Uni<RegisterResponse> register(RegisterRequest body) {
        return telemetryHelper.traceAndMetric("auth.register", () -> authService.registerUser(pb.Auth.RegisterRequest.newBuilder()
                .setFirstname(body.firstname())
                .setLastname(body.lastname())
                .setEmail(body.email())
                .setPassword(body.password())
                .setConfirmPassword(body.confirmPassword())
                .build())
                .map(RegisterResponse::from));
    }

    @Override
    public Uni<LoginResponse> login(LoginRequest body) {
        return telemetryHelper.traceAndMetric("auth.login", () -> authService.loginUser(pb.Auth.LoginRequest.newBuilder()
                .setEmail(body.email())
                .setPassword(body.password())
                .build())
                .map(LoginResponse::from));
    }

    @Override
    public Uni<SimpleResponse> verify(VerifyCodeRequest body) {
        return telemetryHelper.traceAndMetric("auth.verify", () -> authService.verifyCode(pb.Auth.VerifyCodeRequest.newBuilder()
                .setCode(body.code())
                .build())
                .map(SimpleResponse::from));
    }

    @Override
    public Uni<SimpleResponse> forgotPassword(ForgotPasswordRequest body) {
        return telemetryHelper.traceAndMetric("auth.forgotPassword", () -> authService.forgotPassword(pb.Auth.ForgotPasswordRequest.newBuilder()
                .setEmail(body.email())
                .build())
                .map(SimpleResponse::from));
    }

    @Override
    public Uni<SimpleResponse> resetPassword(ResetPasswordRequest body) {
        return telemetryHelper.traceAndMetric("auth.resetPassword", () -> authService.resetPassword(pb.Auth.ResetPasswordRequest.newBuilder()
                .setResetToken(body.resetToken())
                .setPassword(body.password())
                .setConfirmPassword(body.confirmPassword())
                .build())
                .map(SimpleResponse::from));
    }

    @Override
    public Uni<RefreshTokenResponse> refresh(RefreshTokenRequest body) {
        return telemetryHelper.traceAndMetric("auth.refresh", () -> authService.refreshToken(pb.Auth.RefreshTokenRequest.newBuilder()
                .setRefreshToken(body.refreshToken())
                .build())
                .map(RefreshTokenResponse::from));
    }

    @Override
    public Uni<GetMeResponse> getMe(int userId) {
        return telemetryHelper.traceAndMetric("auth.getMe", () -> authService.getMe(pb.Auth.GetMeRequest.newBuilder()
                .setUserId(userId)
                .build())
                .map(GetMeResponse::from));
    }
}
