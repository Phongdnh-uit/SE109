package com.uit.se109.services.mail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.uit.se109.configs.AppProperties;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

  @Mock private JavaMailSender javaMailSender;

  @Mock private ITemplateEngine templateEngine;

  @Mock private AppProperties appProperties;

  @InjectMocks private EmailServiceImpl emailService;

  private MimeMessage mimeMessage;

  @BeforeEach
  void setUp() {
    mimeMessage = mock(MimeMessage.class);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
  }

  @Test
  void sendRegistrationOtp_Success() {
    String to = "test@example.com";
    String otp = "123456";
    String htmlContent = "<html><body>OTP: 123456</body></html>";

    when(templateEngine.process(eq("mail/registration-otp"), any(Context.class)))
        .thenReturn(htmlContent);

    emailService.sendRegistrationOtp(to, otp);

    verify(templateEngine).process(eq("mail/registration-otp"), any(Context.class));
    verify(javaMailSender).send(mimeMessage);
  }

  @Test
  void sendForgotPasswordToken_Success() {
    String to = "test@example.com";
    String token = "uuid-token";
    String htmlContent = "<html><body>Reset: uuid-token</body></html>";

    AppProperties.Frontend frontend = new AppProperties.Frontend();
    frontend.setBaseUrl("http://localhost:3000");
    when(appProperties.getFrontend()).thenReturn(frontend);
    when(templateEngine.process(eq("mail/forgot-password"), any(Context.class)))
        .thenReturn(htmlContent);

    emailService.sendForgotPasswordToken(to, token);

    verify(templateEngine).process(eq("mail/forgot-password"), any(Context.class));
    verify(javaMailSender).send(mimeMessage);
  }
}
