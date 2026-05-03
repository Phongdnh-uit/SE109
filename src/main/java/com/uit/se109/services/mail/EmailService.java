package com.uit.se109.services.mail;

public interface EmailService {
  void sendRegistrationOtp(String to, String otp);

  void sendForgotPasswordToken(String to, String token);
}
