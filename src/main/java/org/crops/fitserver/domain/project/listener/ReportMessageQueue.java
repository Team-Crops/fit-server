package org.crops.fitserver.domain.project.listener;

import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.mq.MessageReceiver;
import org.crops.fitserver.global.mq.dto.Report;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportMessageQueue implements MessageReceiver<Report> {

  @Override
  public void onMessage(Report message) {
    log.debug("Received message: {}", message);
  }
}
