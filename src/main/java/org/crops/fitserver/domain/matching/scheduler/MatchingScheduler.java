package org.crops.fitserver.domain.matching.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.matching.repository.MatchingRepository;
import org.crops.fitserver.domain.matching.repository.MatchingRoomRepository;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class MatchingScheduler {

  private final MatchingService matchingService;
  private final MatchingRepository matchingRepository;
  private final MatchingRoomRepository matchingRoomRepository;
  private final ChatRoomService chatRoomService;


  //매 10초마다
  @Scheduled(cron = "1/10 * * * * *")
  @Transactional
  public void matching() {
    log.info("매칭 시작");
    var matchingProcessor = new MatchingProcessor(matchingRepository, matchingRoomRepository,
        chatRoomService);

    long matchingCount = 0;

    matchingCount += matchingProcessor.insertToNotEnoughRoom();
    matchingCount += matchingProcessor.createNewRoom();
    matchingCount += matchingProcessor.joinRoom();

    log.info("매칭 종료 : {}개 매칭", matchingCount);

  }

  @Scheduled(cron = "0/10 * * * * *")
  @Transactional
  public void expireMatching() {
    var expriredMatchingList = matchingService.expireMatchingAll();

    expriredMatchingList.forEach(matching -> {
      log.info("매칭 만료 : {}", matching.getId());
      //TODO: 만료된 매칭에 대한 알림 처리 로직 추가
    });
  }

}
