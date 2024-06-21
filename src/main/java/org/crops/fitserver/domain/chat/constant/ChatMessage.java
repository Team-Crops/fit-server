package org.crops.fitserver.domain.chat.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ChatMessage {

  FORCED_OUT("%s님을 내보냈습니다."),
  EXIT("%s님이 나갔습니다."),
  JOIN("%s님이 들어왔습니다."),
  COMPLETE("채팅방이 종료되었습니다.");
  private final String message;

  public String getMessage(String keyword) {
    if (keyword == null) {
      return message;
    }
    return String.format(message, keyword);
  }

  public String getMessage() {
    return message;
  }
}
