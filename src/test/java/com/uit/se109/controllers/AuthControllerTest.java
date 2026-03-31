package com.uit.se109.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se109.configs.AppProperties;
import com.uit.se109.configs.CorsConfig;
import com.uit.se109.configs.SecurityConfig;
import com.uit.se109.configs.SwaggerConfig;
import com.uit.se109.dto.auth.ChangePasswordRequest;
import com.uit.se109.dto.auth.LoginRequest;
import com.uit.se109.dto.auth.LoginResponse;
import com.uit.se109.dto.auth.RefreshRequest;
import com.uit.se109.dto.auth.RegisterRequest;
import com.uit.se109.dto.user.UserResponse;
import com.uit.se109.securities.filter.PrometheusSecurityFilter;
import com.uit.se109.securities.jwt.CustomJwtConverter;
import com.uit.se109.services.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = AuthController.class,
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {
              SecurityConfig.class,
              CorsConfig.class,
              SwaggerConfig.class,
              AppProperties.class,
              PrometheusSecurityFilter.class,
              CustomJwtConverter.class
            }))
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @MockitoBean private AuthService authService;

  private LoginRequest validLoginRequest;
  private LoginResponse loginResponse;
  private RegisterRequest validRegisterRequest;
  private UserResponse userResponse;

  @BeforeEach
  void setUp() {
    validLoginRequest = new LoginRequest("testuser", "password123");
    loginResponse = new LoginResponse("access_token_123", "refresh_token_123");

    validRegisterRequest = new RegisterRequest();
    validRegisterRequest.setUsername("newuser");
    validRegisterRequest.setPassword("password123");
    validRegisterRequest.setEmail("newuser@test.com");
    validRegisterRequest.setFullName("New User");
    validRegisterRequest.setPhoneNumber("0123456789");

    userResponse = new UserResponse();
    userResponse.setId(1L);
    userResponse.setUsername("testuser");
    userResponse.setEmail("testuser@test.com");
    userResponse.setFullName("Test User");
  }

  @Test
  @DisplayName("POST /api/v1/auth/login - Success")
  void testLoginSuccess() throws Exception {
    when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.data.accessToken").value("access_token_123"))
        .andExpect(jsonPath("$.data.refreshToken").value("refresh_token_123"));

    verify(authService, times(1)).login(any(LoginRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/auth/login - Invalid Request")
  void testLoginInvalidRequest() throws Exception {
    LoginRequest invalidRequest = new LoginRequest("", "");

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());

    verify(authService, never()).login(any(LoginRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/auth/register - Success")
  void testRegisterSuccess() throws Exception {
    when(authService.register(any(RegisterRequest.class))).thenReturn(userResponse);

    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.data.username").value("testuser"));

    verify(authService, times(1)).register(any(RegisterRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/auth/register - Invalid Email")
  void testRegisterInvalidEmail() throws Exception {
    RegisterRequest invalidRequest = new RegisterRequest();
    invalidRequest.setUsername("newuser");
    invalidRequest.setPassword("password123");
    invalidRequest.setEmail("invalid-email");

    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());

    verify(authService, never()).register(any(RegisterRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/auth/refresh - Success")
  void testRefreshTokenSuccess() throws Exception {
    RefreshRequest refreshRequest = new RefreshRequest("refresh_token_123");
    when(authService.refreshToken(any(RefreshRequest.class))).thenReturn(loginResponse);

    mockMvc
        .perform(
            post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.accessToken").value("access_token_123"));

    verify(authService, times(1)).refreshToken(any(RefreshRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/auth/logout - Success")
  void testLogoutSuccess() throws Exception {
    RefreshRequest logoutRequest = new RefreshRequest("refresh_token_123");
    doNothing().when(authService).logout(any(RefreshRequest.class));

    mockMvc
        .perform(
            post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logoutRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.message").value("success"));

    verify(authService, times(1)).logout(any(RefreshRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/auth/change-password - Success")
  void testChangePasswordSuccess() throws Exception {
    ChangePasswordRequest changePasswordRequest =
        new ChangePasswordRequest("oldpassword123", "newpassword123");

    doNothing().when(authService).changePassword(any(ChangePasswordRequest.class));

    mockMvc
        .perform(
            post("/api/v1/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000));

    verify(authService, times(1)).changePassword(any(ChangePasswordRequest.class));
  }

  @Test
  @DisplayName("GET /api/v1/auth/me - Success")
  void testGetCurrentUserSuccess() throws Exception {
    when(authService.getCurrentUser()).thenReturn(userResponse);

    mockMvc
        .perform(get("/api/v1/auth/me").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.username").value("testuser"));

    verify(authService, times(1)).getCurrentUser();
  }
}
