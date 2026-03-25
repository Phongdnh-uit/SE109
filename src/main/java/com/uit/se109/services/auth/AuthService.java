package com.uit.se109.services.auth;

import com.uit.se109.dto.auth.LoginRequest;
import com.uit.se109.dto.auth.LoginResponse;
import com.uit.se109.dto.auth.RefreshRequest;
import com.uit.se109.dto.auth.RegisterRequest;
import com.uit.se109.dto.user.UserResponse;

public interface AuthService {
  LoginResponse login(LoginRequest request);

  LoginResponse refreshToken(RefreshRequest request);

  // void logout(RefreshTokenRequest request);

  UserResponse register(RegisterRequest request);
  //
  // void verifyEmail(VerifyEmailRequest request);
  //
  // void resetPassword(ResetPasswordRequest request);
  //
  // void changePassword(ChangePasswordRequest request);
  //
  // UserResponse getCurrentUser();
  //
  // UserResponse updateCurrentUser(BaseUserRequest request);
  //
  // List<String> getCurrentPermissionCodes();
}
