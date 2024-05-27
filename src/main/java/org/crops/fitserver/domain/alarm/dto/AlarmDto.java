package org.crops.fitserver.domain.alarm.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.crops.fitserver.domain.alarm.domain.Alarm;
import org.crops.fitserver.domain.alarm.domain.AlarmCase;

@Builder
public record AlarmDto(
    long id,
    AlarmCase alarmCase,
    boolean isRead,
    LocalDateTime createAt
) {

  public static AlarmDto from(Alarm alarm) {
    return AlarmDto.builder()
        .id(alarm.getId())
        .alarmCase(alarm.getAlarmCase())
        .isRead(alarm.isRead())
        .createAt(alarm.getCreatedAt())
        .build();
  }
}
