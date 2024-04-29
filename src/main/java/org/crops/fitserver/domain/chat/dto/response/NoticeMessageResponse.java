package org.crops.fitserver.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.global.socket.service.MessageResponse;

@Getter
public class NoticeMessageResponse extends MessageResponse {

  private String notice;

  @Builder
  private NoticeMessageResponse(
      String notice,
      Long messageId,
      MessageType messageType) {
    this.notice = notice;
    this.messageId = messageId;
    this.messageType = messageType;
  }

  public static NoticeMessageResponse from(Message message) {
    return NoticeMessageResponse.builder()
        .notice(message.getContent())
        .messageId(message.getId())
        .messageType(MessageType.NOTICE)
        .build();
  }
}
