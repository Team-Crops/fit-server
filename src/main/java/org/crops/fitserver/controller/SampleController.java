package org.crops.fitserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    @PostMapping("/sample")
    public ResponseEntity<?> sample() {
        return ResponseEntity.ok().build();
    }
}
