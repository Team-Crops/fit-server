package org.crops.fitserver.domain.chat.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ChatConstant {

  FORCED_OUT("%s님을 내보냈습니다."),
  LEAVE("%s님이 나갔습니다."),
  JOIN("%s님이 들어왔습니다."),
  ;
  private final String message;

  public String getMessage(String name) {
    return String.format(message, name);
  }
}
