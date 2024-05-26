package org.crops.fitserver.domain.alarm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.alarm.dto.response.GetAlarmListResponse;
import org.crops.fitserver.domain.alarm.facade.AlarmFacade;
import org.crops.fitserver.global.annotation.CurrentUserId;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class AlarmController {

  private final AlarmFacade alarmFacade;

  @GetMapping
  public ResponseEntity<GetAlarmListResponse> getAlarmList(
      @CurrentUserId Long userId,
      @RequestParam(defaultValue = "0") int page
  ) {
    var response = alarmFacade.getAlarmList(userId, page);
    return ResponseEntity.ok(response);
  }
}
