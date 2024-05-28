package org.crops.fitserver.domain.alarm.facade.impl;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.alarm.dto.response.GetAlarmListResponse;
import org.crops.fitserver.domain.alarm.facade.AlarmFacade;
import org.crops.fitserver.domain.alarm.service.AlarmService;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AlarmFacadeImpl implements AlarmFacade {

  private final AlarmService alarmService;
  private final UserService userService;

  @Override
  @Transactional
  public GetAlarmListResponse getAlarmList(long userId, int page) {
    User user = userService.getById(userId);
    return GetAlarmListResponse.from(
        alarmService.getAlarmList(user, page));
  }
}
