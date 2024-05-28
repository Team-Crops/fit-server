package org.crops.fitserver.domain.alarm.dto.response;

import org.crops.fitserver.domain.alarm.dto.AlarmDto;
import org.crops.fitserver.global.http.PageResult;

public record GetAlarmListResponse(
    PageResult<AlarmDto> pageResult
) {

  public static GetAlarmListResponse from(PageResult<AlarmDto> pageResult) {
    return new GetAlarmListResponse(pageResult);
  }
}
