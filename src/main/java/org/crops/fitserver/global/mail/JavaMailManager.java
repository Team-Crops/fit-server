package org.crops.fitserver.global.mail;

import jakarta.mail.MessagingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;


@Slf4j
@Service
public class JavaMailManager implements MailManager {

  private final JavaMailSender javaMailSender;

  private final String template;

  private final static String TEMPLATE_PATH = "classpath:templates/mail-template.html";

  //javaMailSender 를 오토와이어링할 수 없다고 뜨나, 실제로는 잘되니 무시
  public JavaMailManager(JavaMailSender javaMailSender, ResourceLoader resourceLoader) {
    this.javaMailSender = javaMailSender;

    try {
      var resource = resourceLoader.getResource(TEMPLATE_PATH);
      var bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
      this.template = new String(bytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load email template", e);
    }
  }


  @Override
  public void send(String to, String subject, String subTemplate) {
    send(new String[]{to}, subject, subTemplate);
  }

  @Override
  public void send(String[] to, String subject, String subTemplate) {
    var message = javaMailSender.createMimeMessage();

    var mailContent = template.replace("${template}", subTemplate);

    //test로 시작하는 문자열 제거
    to = Arrays.stream(to).filter(s -> !s.startsWith("test")).toArray(String[]::new);
    if(ArrayUtils.isEmpty(to)){
      return;
    }

    try {
      var helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setTo(to);
      helper.setSubject("[F-IT] " + subject);
      helper.setText(mailContent, true);


    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send email", e);
    }

    javaMailSender.send(message);
  }
}
