package org.crops.fitserver.domain.alarm.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.crops.fitserver.domain.alarm.domain.Alarm;
import org.crops.fitserver.domain.alarm.domain.AlarmType;

@Builder
public record AlarmDto(
    long id,
    AlarmType type,
    String title,
    String description,
    boolean isRead,
    LocalDateTime alarmTime
) {

  public static AlarmDto from(Alarm alarm) {
    return AlarmDto.builder()
        .id(alarm.getId())
        .type(alarm.getAlarmCase().getAlarmType())
        .title(alarm.getAlarmCase().getTitle())
        .description(alarm.getAlarmCase().getDescription())
        .isRead(alarm.isRead())
        .alarmTime(alarm.getCreatedAt())
        .build();
  }
}
