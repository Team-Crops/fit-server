package org.crops.fitserver.domain.matching.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.matching.dto.MatchingDto;
import org.crops.fitserver.domain.matching.dto.request.ForceOutRequest;
import org.crops.fitserver.domain.matching.dto.request.ReadyMatchingRequest;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingRoomResponse;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.crops.fitserver.global.annotation.CurrentUserId;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingController {

  private final MatchingService matchingService;

  @PostMapping
  public ResponseEntity<MatchingDto> createMatching(
      @CurrentUserId Long userId
  ) {
    return ResponseEntity.ok(matchingService.createMatching(userId));
  }

  @GetMapping
  public ResponseEntity<MatchingDto> getMatching(
      @CurrentUserId Long userId
  ) {
    return ResponseEntity.ok(matchingService.getMatching(userId));
  }


  @PostMapping("/cancel")
  public ResponseEntity<Void> cancelMatching(
      @CurrentUserId Long userId
  ) {
    matchingService.cancel(userId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/room/{roomId}")
  public ResponseEntity<GetMatchingRoomResponse> getMatchingRoom(
      @CurrentUserId Long userId,
      @PathVariable("roomId") Long roomId
  ) {
    return ResponseEntity.ok(matchingService.getMatchingRoom(userId, roomId));
  }

  @PostMapping("/room/{roomId}/force-out")
  public ResponseEntity<Void> forceOut(
      @CurrentUserId Long userId,
      @PathVariable("roomId") Long roomId,
      @RequestBody ForceOutRequest forceOutRequest
  ) {
    matchingService.forceOut(userId, roomId, forceOutRequest.userId());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/room/{roomId}/ready")
  public ResponseEntity<Void> readyMatching(
      @CurrentUserId Long userId,
      @PathVariable("roomId") Long roomId,
      @RequestBody ReadyMatchingRequest readyMatchingRequest
  ) {
    if(readyMatchingRequest.isReady()){
      matchingService.ready(userId, roomId);
    }else{
      matchingService.cancelReady(userId, roomId);
    }
    return ResponseEntity.ok().build();
  }

  @PostMapping("/room/{roomId}/complete")
  public ResponseEntity<Void> completeMatching(
      @CurrentUserId Long userId,
      @PathVariable("roomId") Long roomId
  ) {
    matchingService.complete(userId, roomId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/room/{roomId}/cancel")
  public ResponseEntity<Void> cancelMatching(
      @CurrentUserId Long userId,
      @PathVariable("roomId") Long roomId
  ) {
    matchingService.exit(userId, roomId);
    return ResponseEntity.ok().build();
  }


}
