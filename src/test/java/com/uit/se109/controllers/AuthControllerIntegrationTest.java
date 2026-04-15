package com.uit.se109.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se109.dto.auth.RegisterRequest;
import com.uit.se109.dto.auth.VerifyOtpRequest;
import com.uit.se109.entities.User;
import com.uit.se109.entities.VerificationToken;
import com.uit.se109.enums.UserStatus;
import com.uit.se109.enums.VerificationTokenType;
import com.uit.se109.repositories.UserRepository;
import com.uit.se109.repositories.VerificationTokenRepository;
import com.uit.se109.services.mail.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

  @Container @ServiceConnection static MySQLContainer mysql = new MySQLContainer("mysql:latest");

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private UserRepository userRepository;

  @Autowired private VerificationTokenRepository verificationTokenRepository;

  @MockBean private EmailService emailService;

  @Test
  void testRegisterAndVerifyOtpFlow() throws Exception {
    // 1. Register
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setUsername("testuser");
    registerRequest.setPassword("Password123!");
    registerRequest.setEmail("test@example.com");
    registerRequest.setFullName("Test User");
    registerRequest.setPhoneNumber("0987654321");

    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.username").value("testuser"));

    // Verify email was sent
    verify(emailService).sendRegistrationOtp(anyString(), anyString());

    // Verify user is PENDING
    User user = userRepository.findByUsername("testuser").orElseThrow();
    Assertions.assertEquals(UserStatus.PENDING, user.getStatus());

    // Get OTP from DB
    VerificationToken token =
        verificationTokenRepository.findAll().stream()
            .filter(
                t ->
                    t.getUser().getId().equals(user.getId())
                        && t.getType() == VerificationTokenType.REGISTRATION_OTP)
            .findFirst()
            .orElseThrow();
    String otp = token.getToken();

    // 2. Verify OTP
    VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest("test@example.com", otp);

    mockMvc
        .perform(
            post("/api/v1/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyOtpRequest)))
        .andExpect(status().isOk());

    // Verify user is ACTIVE
    user = userRepository.findByUsername("testuser").orElseThrow();
    Assertions.assertEquals(UserStatus.ACTIVE, user.getStatus());

    // Verify token is deleted
    Assertions.assertFalse(verificationTokenRepository.existsById(token.getId()));
  }

  @Test
  void testVerifyOtpInvalid() throws Exception {
    // Setup: register a user first
    User user = new User();
    user.setUsername("invaliduser");
    user.setEmail("invalid@example.com");
    user.setPassword("password");
    user.setStatus(UserStatus.PENDING);
    userRepository.save(user);

    VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest("invalid@example.com", "000000");

    mockMvc
        .perform(
            post("/api/v1/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyOtpRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(3005));
  }
}
