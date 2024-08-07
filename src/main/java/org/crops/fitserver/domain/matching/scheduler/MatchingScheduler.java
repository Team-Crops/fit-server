package org.crops.fitserver.domain.matching.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.alarm.domain.AlarmCase;
import org.crops.fitserver.domain.alarm.service.AlarmService;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MatchingScheduler {

  private final MatchingProcessor matchingProcessor;
  private final MatchingService matchingService;
  private final AlarmService alarmService;

  @Scheduled(cron = "0 */2 * * * *")
  @Async
  public void matching() {
    log.info("매칭 시작");
    matchingProcessor.match();
    log.info("매칭 종료");
  }


  @Scheduled(cron = "0 */30 * * * *")
  @Async
  public void expireMatching() {
    var expriredMatchingList = matchingService.expireMatchingAll();

    expriredMatchingList.forEach(matching -> {
      log.info("매칭 만료 : {}", matching.getId());
      alarmService.sendAlarm(matching.getUser(), AlarmCase.FAILED_MATCHING);
    });
  }

}
