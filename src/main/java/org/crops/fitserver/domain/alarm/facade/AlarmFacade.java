package org.crops.fitserver.domain.alarm.facade;

import org.crops.fitserver.domain.alarm.dto.response.GetAlarmListResponse;

public interface AlarmFacade {

  GetAlarmListResponse getAlarmList(long userId, int page);
}
