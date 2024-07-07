package org.crops.fitserver.global.mail;

public interface MailManager {

  void send(String to, String subject, String template);

  void send(String[] to, String subject, String template);
}
