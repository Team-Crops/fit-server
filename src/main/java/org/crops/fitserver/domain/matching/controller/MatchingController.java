package org.crops.fitserver.domain.matching.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.matching.dto.response.CreateMatchingResponse;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingResponse;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingRoomResponse;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.crops.fitserver.global.annotation.CurrentUserId;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingController {

  private final MatchingService matchingService;

  @PostMapping("/")
  public ResponseEntity<CreateMatchingResponse> createMatching(
      @CurrentUserId Long userId
  ) {
    return ResponseEntity.ok(matchingService.createMatching(userId));
  }

  @GetMapping("/")
  public ResponseEntity<GetMatchingResponse> getMatching(
      @CurrentUserId Long userId
  ) {
    return ResponseEntity.ok(matchingService.getMatching(userId));
  }

  @GetMapping("/room/{roomId}")
  public ResponseEntity<GetMatchingRoomResponse> getMatchingRoom(
      @CurrentUserId Long userId,
      @PathVariable("roomId") Long roomId
  ) {
    return ResponseEntity.ok(matchingService.getMatchingRoom(userId, roomId));
  }

  @PostMapping("/cancel")
  public ResponseEntity<Void> cancelMatching(
      @CurrentUserId Long userId
  ) {
    matchingService.cancelMatching(userId);
    return ResponseEntity.ok().build();
  }


}
