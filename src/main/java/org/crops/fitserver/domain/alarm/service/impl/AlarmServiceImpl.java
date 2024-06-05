package org.crops.fitserver.domain.alarm.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.alarm.domain.Alarm;
import org.crops.fitserver.domain.alarm.domain.AlarmCase;
import org.crops.fitserver.domain.alarm.dto.AlarmDto;
import org.crops.fitserver.domain.alarm.repository.AlarmRepository;
import org.crops.fitserver.domain.alarm.service.AlarmService;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.global.http.PageResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

  private final AlarmRepository alarmRepository;

  private static final int DEFAULT_PAGE_SIZE = 10;

  @Override
  @Transactional
  public PageResult<AlarmDto> getAlarmList(User user, int page) {
    Slice<Alarm> slice = alarmRepository.getAllByUserIdRecent(
        user.getId(), PageRequest.of(page, DEFAULT_PAGE_SIZE));
    List<AlarmDto> list = slice
        .getContent()
        .stream()
        .map(AlarmDto::from)
        .toList();
    alarmRepository.updateReadByIdList(
        slice.getContent()
            .stream()
            .map(Alarm::getId).toList());
    return PageResult.of(
        list,
        slice.hasNext());
  }

  @Override
  public void sendAlarm(User user, AlarmCase alarmCase) {
    Alarm alarm = Alarm.create(user, alarmCase);
    alarmRepository.save(alarm);
  }

  @Override
  public void sendAlarmIfNotRead(User user, AlarmCase alarmCase) {
    if (alarmRepository.existsByUserIdAndAlarmCaseAndIsReadTrue(user.getId(), alarmCase)) {
      sendAlarm(user, alarmCase);
    }
  }
}
