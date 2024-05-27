package org.crops.fitserver.global.mail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class JavaMailManagerTest {

  @Autowired
  JavaMailManager javaMailHelper;

  @Autowired
  JavaMailSender javaMailSender;

  @Test
  void send() {
//    javaMailHelper.send("aal1324@naver.com","test","test");
  }
}