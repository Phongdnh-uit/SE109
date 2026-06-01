package com.uit.se109.services.mail;

import com.uit.se109.configs.AppProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;
  private final ITemplateEngine templateEngine;
  private final AppProperties appProperties;

  @Async
  @Override
  public void sendRegistrationOtp(String to, String otp) {
    Context context = new Context();
    context.setVariable("otp", otp);
    String content = templateEngine.process("mail/registration-otp", context);
    sendEmail(to, "Email Verification", content);
  }

  @Async
  @Override
  public void sendForgotPasswordToken(String to, String token) {
    Context context = new Context();
    context.setVariable("token", token);
    context.setVariable("frontendBase", appProperties.getFrontend().getBaseUrl());
    String content = templateEngine.process("mail/forgot-password", context);
    sendEmail(to, "Reset Password", content);
  }

  private void sendEmail(String to, String subject, String content) {
    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(content, true);
      javaMailSender.send(mimeMessage);
    } catch (MessagingException e) {
      log.error("Failed to send email to {}", to, e);
      throw new RuntimeException("Failed to send email", e);
    }
  }
}
