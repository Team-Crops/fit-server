package org.crops.fitserver.domain.matching.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DummyMatchingScheduler {
  private final DummyMatchingProcessor dummyMatchingProcessor;
  private final MatchingService matchingService;

  @Scheduled(cron = "0/30 * * * * *")
  public void InsertDummyMatching() {

    dummyMatchingProcessor.validateSkillSet();
    var newUsers = dummyMatchingProcessor.prepareUser();

    for (var user : newUsers) {
      matchingService.createMatching(user.getId());
    }
  }
}