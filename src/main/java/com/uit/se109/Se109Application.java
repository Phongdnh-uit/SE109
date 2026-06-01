package com.uit.se109;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Se109Application {

  public static void main(String[] args) {
    SpringApplication.run(Se109Application.class, args);
  }
}
