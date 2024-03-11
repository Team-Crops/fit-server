package org.crops.fitserver.domain.matching.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MatchingScheduler {

  private final MatchingService matchingService;


  //매 10초마다
  @Scheduled(cron = "0/10 * * * * *")
  public void matching() {

  }

  @Scheduled(cron = "0/10 * * * * *")
  public void expireMatching() {
    var expriredMatchingList = matchingService.expireMatchingAll();

    expriredMatchingList.forEach(matching -> {
      log.info("매칭 만료 : {}", matching.getId());
      //TODO: 만료된 매칭에 대한 알림 처리 로직 추가
    });
  }

}
