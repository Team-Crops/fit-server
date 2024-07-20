package org.crops.fitserver.domain.mail.dto;

import static org.crops.fitserver.domain.mail.constants.MailConstants.DOMAIN;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.mail.constants.MailTemplates;

@Getter
public enum UserMailType implements MailType {
  CREATE_MATCHING_ROOM("대기방 생성 안내", MailTemplates.CREATE_MATCHING_ROOM_TEMPLATE, "/matching"),
  CREATE_MATCHING_ROOM_FOR_HOST("대기방 생성 안내", MailTemplates.CREATE_MATCHING_ROOM_FOR_HOST_TEMPLATE, "/matching"),
  DONE_READY_COMPLETE("매칭 준비 완료 안내", MailTemplates.DONE_READY_COMPLETE_TEMPLATE, "/matching"),
  START_PROJECT("프로젝트 시작 안내", MailTemplates.START_PROJECT_TEMPLATE, "/projects")
  ,
  ;

  private final String subject;
  private final String template;
  private final String url;

  UserMailType(String subject, String template, String url) {
    this.subject = subject;
    this.template = MailTemplates.DEFAULT_TEMPLATE.replace("${content}", template);
    this.url = url;
  }

  public String replaceBy(MailRequiredInfo mailRequiredInfo) {
    return mailRequiredInfo.replace(template)
        .replace("${url}", DOMAIN + url);
  }
}
