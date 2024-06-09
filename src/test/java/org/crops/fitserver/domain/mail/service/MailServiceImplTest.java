package org.crops.fitserver.domain.mail.service;

import static org.junit.jupiter.api.Assertions.*;

import org.crops.fitserver.domain.mail.dto.DefaultMailRequiredInfo;
import org.crops.fitserver.domain.mail.dto.MailRequiredInfo;
import org.crops.fitserver.domain.mail.dto.MailType;
import org.crops.fitserver.domain.mail.dto.UserMailType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class MailServiceImplTest {

  @Autowired
  private MailService mailService;
  @Test
  void send() {
    mailService.send(UserMailType.CREATE_MATCHING_ROOM, "aal1324@naver.com",
        DefaultMailRequiredInfo.of("박준찬의닉네임"));
  }
}