package com.sanedge.gateway.service;

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
import io.smallrye.mutiny.Uni;

public interface AuthService {
    Uni<RegisterResponse> register(RegisterRequest body);
    Uni<LoginResponse> login(LoginRequest body);
    Uni<SimpleResponse> verify(VerifyCodeRequest body);
    Uni<SimpleResponse> forgotPassword(ForgotPasswordRequest body);
    Uni<SimpleResponse> resetPassword(ResetPasswordRequest body);
    Uni<RefreshTokenResponse> refresh(RefreshTokenRequest body);
    Uni<GetMeResponse> getMe(int userId);
}
