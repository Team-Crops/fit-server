package org.crops.fitserver.domain.mail.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserMailType implements MailType {
  CREATE_MATCHING_ROOM("대기방 생성 안내", MailTemplate.CREATE_MATCHING_ROOM_TEMPLATE),
  CREATE_MATCHING_ROOM_FOR_HOST("대기방 생성 안내", MailTemplate.CREATE_MATCHING_ROOM_FOR_HOST_TEMPLATE),
  DONE_READY_COMPLETE("매칭 준비 완료 안내", MailTemplate.DONE_READY_COMPLETE_TEMPLATE),
  START_PROJECT("프로젝트 시작 안내", MailTemplate.START_PROJECT_TEMPLATE),
  ;

  private final String subject;
  private final String template;

  public String replaceBy(MailRequiredInfo mailRequiredInfo) {
    return mailRequiredInfo.replace(template);
  }
}
