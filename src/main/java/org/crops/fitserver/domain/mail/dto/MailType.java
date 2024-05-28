package org.crops.fitserver.domain.mail.dto;

public interface MailType {
  String getSubject();
  String getTemplate();
  String replaceBy(MailRequiredInfo mailRequiredInfo);
}
