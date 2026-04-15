package com.uit.se109.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.uit.se109.entities.User;
import com.uit.se109.entities.VerificationToken;
import com.uit.se109.enums.VerificationTokenType;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class VerificationTokenRepositoryTest {

  @Container @ServiceConnection static MySQLContainer mysql = new MySQLContainer("mysql:latest");

  @Autowired private VerificationTokenRepository verificationTokenRepository;

  @Autowired private UserRepository userRepository;

  @Test
  void testSaveAndFindVerificationToken() {
    User user = new User();
    user.setUsername("testuser");
    user.setPassword("password");
    user.setEmail("test@example.org");
    user.setFullName("Test User");
    user.setCreatedAt(Instant.now());
    user.setUpdatedAt(Instant.now());
    userRepository.save(user);

    VerificationToken token = new VerificationToken();
    token.setUser(user);
    token.setToken("123456");
    token.setType(VerificationTokenType.REGISTRATION_OTP);
    token.setExpiryAt(Instant.now().plus(1, ChronoUnit.HOURS));
    token.setCreatedAt(Instant.now());
    token.setUpdatedAt(Instant.now());

    VerificationToken savedToken = verificationTokenRepository.save(token);
    assertThat(savedToken.getId()).isNotNull();

    Optional<VerificationToken> foundToken =
        verificationTokenRepository.findById(savedToken.getId());
    assertThat(foundToken).isPresent();
    assertThat(foundToken.get().getToken()).isEqualTo("123456");
    assertThat(foundToken.get().getUser().getUsername()).isEqualTo("testuser");
    assertThat(foundToken.get().getType()).isEqualTo(VerificationTokenType.REGISTRATION_OTP);
  }
}
