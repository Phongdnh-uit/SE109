package com.uit.se109.services.auth;

import com.uit.se109.dto.auth.LoginRequest;
import com.uit.se109.dto.auth.LoginResponse;
import com.uit.se109.dto.auth.RefreshRequest;
import com.uit.se109.dto.auth.RegisterRequest;
import com.uit.se109.dto.user.UserResponse;
import com.uit.se109.entities.User;
import com.uit.se109.exception.AppException;
import com.uit.se109.exception.ErrorCode;
import com.uit.se109.helpers.ValidationHelper;
import com.uit.se109.mappers.UserMapper;
import com.uit.se109.repositories.UserRepository;
import com.uit.se109.securities.JwtProvider;
import com.uit.se109.securities.SecurityUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final UserMapper userMapper;

  @Override
  public LoginResponse login(LoginRequest request) {
    // 1. ---- Authenticate ----
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(request.credential(), request.password());
    Authentication authentication =
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    // 2. ---- Set to security holder  ----
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 3. ---- Generate JWT ----
    Long userId = SecurityUtil.getCurrentUserId();
    String accessToken = jwtProvider.generateToken(userId);
    String refreshToken = jwtProvider.generateToken(userId);

    // 4. ---- Response ----
    LoginResponse response = new LoginResponse(accessToken, refreshToken);
    return response;
  }

  @Override
  public LoginResponse refreshToken(RefreshRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
  }

  @Override
  public UserResponse register(RegisterRequest request) {
    Map<String, String> errors = new HashMap<>();
    // Check (phone, email, username)
    if (ValidationHelper.exists("email", request.getEmail(), userRepository)) {
      errors.put("email", "Email already exists");
    }
    if (ValidationHelper.exists("phoneNumber", request.getPhoneNumber(), userRepository)) {
      errors.put("phone", "Phone already exists");
    }
    if (ValidationHelper.exists("username", request.getUsername(), userRepository)) {
      errors.put("username", "Username already exists");
    }
    if (!errors.isEmpty()) {
      throw new AppException(ErrorCode.VALIDATION_ERROR, errors);
    }

    User user = userMapper.requestToEntity(request);

    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user = userRepository.save(user);
    return userMapper.entityToResponse(user);
  }
}
