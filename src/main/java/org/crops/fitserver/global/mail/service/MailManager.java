package org.crops.fitserver.global.mail.service;

public interface MailManager {

  void send(String to, String subject, String content);

  void send(String[] to, String subject, String content);
}
