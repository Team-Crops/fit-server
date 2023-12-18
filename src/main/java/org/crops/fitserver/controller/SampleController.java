package org.crops.fitserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  @GetMapping("/sample")
  public ResponseEntity<?> sample() {
    return ResponseEntity.ok().build();
  }
}
