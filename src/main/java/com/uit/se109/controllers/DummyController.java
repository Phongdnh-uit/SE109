package com.uit.se109.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
  @GetMapping("/favicon.ico")
  public ResponseEntity<Void> returnNoFavicon() {
    // Trả về HTTP 200 OK nhưng không có nội dung body
    return ResponseEntity.ok().build();
  }
}
