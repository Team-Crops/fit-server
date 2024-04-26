package org.crops.fitserver.domain.project.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.crops.fitserver.domain.user.service.UserBlockService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.mq.MessageReceiver;
import org.crops.fitserver.global.mq.dto.Report;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportMessageQueue implements MessageReceiver<Report> {

  private final UserBlockService userBlockService;
  private final MatchingService matchingService;

  @Override
  public void onMessage(Report message) {
    var targetUserId = message.getTargetUserId();
    /**
     * @유서린
     * canExecute() -> execute() 패턴을 이용할지,
     * try-catch-ignore 패턴을 이용할지 고민입니다.
     * */
    if (userBlockService.canBlockUser(targetUserId)) {
      userBlockService.blockUser(message.getTargetUserId());
    }
    try {
      matchingService.cancel(targetUserId);
    } catch (BusinessException ignored) {

    }
  }
}
