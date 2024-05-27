package org.crops.fitserver.domain.mail.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.mail.constants.MailTemplates;

@RequiredArgsConstructor
@Getter
public enum AdminMailType implements MailType {
  REPORT("신고 접수", MailTemplates.REPORT_TEMPLATE),
  ;

  private final String subject;
  private final String template;

  @Override
  public String replaceBy(MailRequiredInfo mailRequiredInfo) {
    return mailRequiredInfo.replace(template);
  }
}
