package org.crops.fitserver.domain.project.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.user.service.UserBlockService;
import org.crops.fitserver.global.mq.MessageReceiver;
import org.crops.fitserver.global.mq.dto.Report;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportMessageQueue implements MessageReceiver<Report> {
  private final UserBlockService userBlockService;

  @Override
  public void onMessage(Report message) {
    userBlockService.blockUser(message.getTargetUserId());
  }
}
