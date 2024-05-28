package org.crops.fitserver.domain.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.mail.dto.MailRequiredInfo;
import org.crops.fitserver.domain.mail.dto.MailType;
import org.crops.fitserver.global.mail.MailManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

  private final MailManager mailManager;

  @Override
  @Async
  public void send(MailType mailType, String email, MailRequiredInfo mailRequiredInfo) {
    try{
      mailManager.send(email, mailType.getSubject(), mailType.replaceBy(mailRequiredInfo));
    } catch (Exception e){
      log.error("Failed to send email", e);
      //메일 전송 에러가 났으면 무시하고 진행.
    }

  }
}
