package com.uit.se109.services.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uit.se109.configs.AppProperties;
import com.uit.se109.dto.auth.LoginRequest;
import com.uit.se109.dto.auth.LoginResponse;
import com.uit.se109.dto.auth.RefreshRequest;
import com.uit.se109.dto.auth.RegisterRequest;
import com.uit.se109.dto.user.UserResponse;
import com.uit.se109.entities.RefreshToken;
import com.uit.se109.entities.User;
import com.uit.se109.exception.AppException;
import com.uit.se109.exception.ErrorCode;
import com.uit.se109.mappers.UserMapper;
import com.uit.se109.repositories.RefreshTokenRepository;
import com.uit.se109.repositories.UserRepository;
import com.uit.se109.securities.CustomUserDetails;
import com.uit.se109.securities.JwtProvider;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private JwtProvider jwtProvider;
  @Mock private AuthenticationManagerBuilder authenticationManagerBuilder;
  @Mock private UserMapper userMapper;
  @Mock private RefreshTokenRepository refreshTokenRepository;
  @Mock private AppProperties appProperties;

  @InjectMocks private AuthServiceImpl authService;

  private User user;
  private RefreshToken refreshToken;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setUsername("testuser");
    user.setPassword("encodedPassword");

    refreshToken = new RefreshToken();
    refreshToken.setId(1L);
    refreshToken.setToken("old-refresh-token");
    refreshToken.setUser(user);
    refreshToken.setExpiryAt(Instant.now().plusMillis(100000));
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldLoginSuccessfully() throws Exception {
    // Arrange
    LoginRequest loginRequest = new LoginRequest("testuser", "password");
    AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    
    CustomUserDetails userDetails = CustomUserDetails.builder().id(1L).username("testuser").build();
    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    
    when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
    when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
    
    when(jwtProvider.generateToken(1L)).thenReturn("access-token").thenReturn("refresh-token");
    when(userRepository.getReferenceById(1L)).thenReturn(user);
    
    AppProperties.Security securityProps = new AppProperties.Security();
    securityProps.setRefreshTokenExpirationInMillis(86400000L);
    when(appProperties.getSecurity()).thenReturn(securityProps);
    when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

    // Act
    LoginResponse response = authService.login(loginRequest);

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.accessToken()).isEqualTo("access-token");
    assertThat(response.refreshToken()).isEqualTo("refresh-token");
    verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
  }

  @Test
  void shouldRegisterSuccessfully() {
    // Arrange
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setUsername("newuser");
    registerRequest.setPassword("password");
    registerRequest.setEmail("new@example.com");

    UserResponse userResponse = new UserResponse();
    userResponse.setUsername("newuser");

    when(userRepository.exists(any(Specification.class))).thenReturn(false);
    when(userMapper.requestToEntity(any(RegisterRequest.class))).thenReturn(user);
    when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(userMapper.entityToResponse(user)).thenReturn(userResponse);

    // Act
    UserResponse response = authService.register(registerRequest);

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.getUsername()).isEqualTo("newuser");
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void shouldThrowExceptionWhenRegisterWithExistingEmail() {
    // Arrange
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setUsername("newuser");
    registerRequest.setPassword("password");
    registerRequest.setEmail("existing@example.com");

    when(userRepository.exists(any(Specification.class))).thenReturn(true).thenReturn(false).thenReturn(false);

    // Act & Assert
    assertThatThrownBy(() -> authService.register(registerRequest))
        .isInstanceOf(AppException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.VALIDATION_ERROR);
  }

  @Test
  void shouldRefreshTokenSuccessfully() {
    // Arrange
    RefreshRequest refreshRequest = new RefreshRequest("old-refresh-token");

    when(refreshTokenRepository.findOne(any(Specification.class))).thenReturn(Optional.of(refreshToken));
    when(jwtProvider.generateToken(1L)).thenReturn("new-access-token").thenReturn("new-refresh-token");
    
    AppProperties.Security securityProps = new AppProperties.Security();
    securityProps.setRefreshTokenExpirationInMillis(86400000L);
    when(appProperties.getSecurity()).thenReturn(securityProps);

    // Act
    LoginResponse response = authService.refreshToken(refreshRequest);

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.accessToken()).isEqualTo("new-access-token");
    assertThat(response.refreshToken()).isEqualTo("new-refresh-token");
    verify(refreshTokenRepository, times(1)).save(refreshToken);
  }

  @Test
  void shouldThrowExceptionWhenRefreshTokenExpired() {
    // Arrange
    RefreshRequest refreshRequest = new RefreshRequest("old-refresh-token");
    refreshToken.setExpiryAt(Instant.now().minusMillis(100000)); // expired

    when(refreshTokenRepository.findOne(any(Specification.class))).thenReturn(Optional.of(refreshToken));

    // Act & Assert
    assertThatThrownBy(() -> authService.refreshToken(refreshRequest))
        .isInstanceOf(AppException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REFRESH_TOKEN_EXPIRED);
    
    verify(refreshTokenRepository, times(1)).delete(refreshToken);
  }
}
