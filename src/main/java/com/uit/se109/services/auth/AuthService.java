package com.uit.se109.services.auth;

import com.uit.se109.dto.auth.ChangePasswordRequest;
import com.uit.se109.dto.auth.ForgotPasswordRequest;
import com.uit.se109.dto.auth.LoginRequest;
import com.uit.se109.dto.auth.LoginResponse;
import com.uit.se109.dto.auth.RefreshRequest;
import com.uit.se109.dto.auth.RegisterRequest;
import com.uit.se109.dto.auth.ResetPasswordRequest;
import com.uit.se109.dto.auth.VerifyOtpRequest;
import com.uit.se109.dto.user.UserResponse;

public interface AuthService {
  LoginResponse login(LoginRequest request);

  LoginResponse refreshToken(RefreshRequest request);

  void logout(RefreshRequest request);

  UserResponse register(RegisterRequest request);

  void changePassword(ChangePasswordRequest request);

  UserResponse getCurrentUser();

  void verifyOtp(VerifyOtpRequest request);

  void forgotPassword(ForgotPasswordRequest request);

  void resetPassword(ResetPasswordRequest request);
}
