package org.crops.fitserver.domain.matching.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  /**
   * TODO: 크론탭 환경변수에 따라 관리 필요
   */
//  @Scheduled(cron = "0 */3 9-23 * * *")
  @Async
  public void matching() {
    log.info("매칭 시작");
    matchingProcessor.match();
    log.info("매칭 종료");
  }


  @Scheduled(cron = "*/30 * 9-23 * * *")
  @Async
  public void expireMatching() {
    var expriredMatchingList = matchingService.expireMatchingAll();

    expriredMatchingList.forEach(matching -> {
      log.info("매칭 만료 : {}", matching.getId());
      //TODO: 만료된 매칭에 대한 알림 처리 로직 추가
    });
  }

}
