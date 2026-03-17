package com.uit.se109;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Se109Application {

  public static void main(String[] args) {
    SpringApplication.run(Se109Application.class, args);
  }
}
