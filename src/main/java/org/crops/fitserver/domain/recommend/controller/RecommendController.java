package org.crops.fitserver.domain.recommend.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.recommend.dto.RecommendUserDto;
import org.crops.fitserver.domain.recommend.dto.request.RecommendUserRequest;
import org.crops.fitserver.domain.recommend.dto.response.RecommendUserResponse;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

//  private final RecommendFacade recommendFacade;

  @GetMapping("/user")
  public ResponseEntity<RecommendUserResponse> recommendUser(
      @ModelAttribute RecommendUserRequest request
  ) {
    List<RecommendUserDto> response = new ArrayList<>();
    return ResponseEntity.ok(RecommendUserResponse.of(response));
  }

  @PostMapping("/like/user/{userId}")
  public void likeUser() {
    return;
  }
}