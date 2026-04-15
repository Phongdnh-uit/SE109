package com.uit.se109.services.auth;

import com.uit.se109.configs.AppProperties;
import com.uit.se109.dto.auth.ChangePasswordRequest;
import com.uit.se109.dto.auth.ForgotPasswordRequest;
import com.uit.se109.dto.auth.LoginRequest;
import com.uit.se109.dto.auth.LoginResponse;
import com.uit.se109.dto.auth.RefreshRequest;
import com.uit.se109.dto.auth.RegisterRequest;
import com.uit.se109.dto.auth.ResetPasswordRequest;
import com.uit.se109.dto.auth.VerifyOtpRequest;
import com.uit.se109.dto.user.UserResponse;
import com.uit.se109.entities.RefreshToken;
import com.uit.se109.entities.User;
import com.uit.se109.entities.VerificationToken;
import com.uit.se109.enums.UserStatus;
import com.uit.se109.enums.VerificationTokenType;
import com.uit.se109.exception.AppException;
import com.uit.se109.exception.ErrorCode;
import com.uit.se109.helpers.ValidationHelper;
import com.uit.se109.mappers.UserMapper;
import com.uit.se109.repositories.RefreshTokenRepository;
import com.uit.se109.repositories.UserRepository;
import com.uit.se109.repositories.VerificationTokenRepository;
import com.uit.se109.securities.JwtProvider;
import com.uit.se109.securities.SecurityUtil;
import com.uit.se109.services.mail.EmailService;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
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
  private final RefreshTokenRepository refreshTokenRepository;
  private final AppProperties appProperties;
  private final VerificationTokenRepository verificationTokenRepository;
  private final EmailService emailService;

  @Override
  public LoginResponse login(LoginRequest request) {
    User user =
        userRepository
            .findByUsername(request.credential())
            .or(() -> userRepository.findByEmail(request.credential()))
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIAL));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new AppException(ErrorCode.INVALID_CREDENTIAL);
    }

    if (user.getStatus() != UserStatus.ACTIVE) {
      throw new AppException(ErrorCode.USER_NOT_ACTIVE);
    }

    // 1. ---- Authenticate ----
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(user.getUsername(), request.password());
    Authentication authentication =
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    // 2. ---- Set to security holder  ----
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 3. ---- Generate JWT ----
    Long userId = user.getId();
    String accessToken = jwtProvider.generateToken(userId);
    String refreshToken = jwtProvider.generateToken(userId);
    RefreshToken refreshTokenEntity = new RefreshToken();
    refreshTokenEntity.setUser(userRepository.getReferenceById(userId));
    refreshTokenEntity.setToken(refreshToken);
    refreshTokenEntity.setExpiryAt(
        Instant.now().plusMillis(appProperties.getSecurity().getRefreshTokenExpirationInMillis()));
    refreshTokenRepository.save(refreshTokenEntity);

    // 4. ---- Response ----
    LoginResponse response = new LoginResponse(accessToken, refreshToken);
    return response;
  }

  @Override
  public LoginResponse refreshToken(RefreshRequest request) {
    String requestRefreshToken = request.refreshToken();
    RefreshToken refreshTokenEntity =
        refreshTokenRepository
            .findOne(
                (root, query, builder) -> builder.equal(root.get("token"), requestRefreshToken))
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));
    if (refreshTokenEntity.getExpiryAt().isBefore(Instant.now())) {
      refreshTokenRepository.delete(refreshTokenEntity);
      throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }
    Long userId = refreshTokenEntity.getUser().getId();
    String accessToken = jwtProvider.generateToken(userId);
    String refreshToken = jwtProvider.generateToken(userId);
    refreshTokenEntity.setToken(refreshToken);
    refreshTokenEntity.setExpiryAt(
        Instant.now().plusMillis(appProperties.getSecurity().getRefreshTokenExpirationInMillis()));
    refreshTokenRepository.save(refreshTokenEntity);

    return new LoginResponse(accessToken, refreshToken);
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
    user.setStatus(UserStatus.PENDING);
    user = userRepository.save(user);

    // Generate OTP
    String otp = String.format("%06d", new Random().nextInt(999999));
    VerificationToken verificationToken = new VerificationToken();
    verificationToken.setUser(user);
    verificationToken.setToken(otp);
    verificationToken.setType(VerificationTokenType.REGISTRATION_OTP);
    verificationToken.setExpiryAt(Instant.now().plusSeconds(300)); // 5 mins
    verificationTokenRepository.save(verificationToken);

    emailService.sendRegistrationOtp(user.getEmail(), otp);

    return userMapper.entityToResponse(user);
  }

  @Override
  public void logout(RefreshRequest request) {
    String requestRefreshToken = request.refreshToken();
    refreshTokenRepository
        .findOne((root, query, builder) -> builder.equal(root.get("token"), requestRefreshToken))
        .ifPresent(refreshTokenRepository::delete);
  }

  @Override
  public void changePassword(ChangePasswordRequest request) {
    User user =
        userRepository
            .findById(SecurityUtil.getCurrentUserId())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
      throw new AppException(ErrorCode.INVALID_CREDENTIAL);
    }
    user.setPassword(passwordEncoder.encode(request.newPassword()));
    userRepository.save(user);
  }

  @Override
  public UserResponse getCurrentUser() {
    User user =
        userRepository
            .findById(SecurityUtil.getCurrentUserId())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    return userMapper.entityToResponse(user);
  }

  @Override
  public void verifyOtp(VerifyOtpRequest request) {
    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    VerificationToken token =
        verificationTokenRepository
            .findByTokenAndUserAndType(request.otp(), user, VerificationTokenType.REGISTRATION_OTP)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

    if (token.getExpiryAt().isBefore(Instant.now())) {
      verificationTokenRepository.delete(token);
      throw new AppException(ErrorCode.TOKEN_EXPIRED);
    }

    user.setStatus(UserStatus.ACTIVE);
    userRepository.save(user);
    verificationTokenRepository.delete(token);
  }

  @Override
  public void forgotPassword(ForgotPasswordRequest request) {
    User user =
        userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    String token = UUID.randomUUID().toString();
    VerificationToken verificationToken = new VerificationToken();
    verificationToken.setUser(user);
    verificationToken.setToken(token);
    verificationToken.setType(VerificationTokenType.PASSWORD_RESET_TOKEN);
    verificationToken.setExpiryAt(Instant.now().plusSeconds(900)); // 15 mins
    verificationTokenRepository.save(verificationToken);

    emailService.sendForgotPasswordToken(user.getEmail(), token);
  }

  @Override
  public void resetPassword(ResetPasswordRequest request) {
    VerificationToken tokenEntity =
        verificationTokenRepository
            .findByTokenAndType(request.getToken(), VerificationTokenType.PASSWORD_RESET_TOKEN)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

    if (tokenEntity.getExpiryAt().isBefore(Instant.now())) {
      verificationTokenRepository.delete(tokenEntity);
      throw new AppException(ErrorCode.TOKEN_EXPIRED);
    }

    User user = tokenEntity.getUser();
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
    verificationTokenRepository.delete(tokenEntity);
  }
}
