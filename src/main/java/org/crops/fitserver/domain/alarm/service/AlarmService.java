package org.crops.fitserver.domain.alarm.service;

import org.crops.fitserver.domain.alarm.domain.AlarmCase;
import org.crops.fitserver.domain.alarm.dto.AlarmDto;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.global.http.PageResult;

public interface AlarmService {

  PageResult<AlarmDto> getAlarmList(User user, int page);

  void sendAlarm(User user, AlarmCase alarmCase);

  void sendAlarmIfNotRead(User user, AlarmCase alarmCase);
}
