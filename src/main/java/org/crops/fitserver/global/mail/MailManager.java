package org.crops.fitserver.global.mail;

public interface MailManager {

  void send(String to, String subject, String content);

  void send(String[] to, String subject, String content);
}
