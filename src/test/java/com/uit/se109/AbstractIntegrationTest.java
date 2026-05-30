package com.uit.se109;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

  @ServiceConnection static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33");

  static {
    mysql.start();
  }
}
