package org.crops.fitserver.domain.recommend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.recommend.facade.RecommendFacade;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

  private final RecommendFacade recommendFacade;

  @GetMapping
  public void recommendUser() {
    return;
  }

  @PostMapping("/like/user/{userId}")
  public void likeUser() {
    return;
  }
}
