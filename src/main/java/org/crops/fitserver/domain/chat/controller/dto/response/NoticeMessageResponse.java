package org.crops.fitserver.domain.chat.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.global.socket.service.SocketResponse;

@Getter
public class NoticeMessageResponse extends SocketResponse {

  private String notice;

  @Builder
  private NoticeMessageResponse(String notice, MessageType messageType) {
    this.notice = notice;
    this.messageType = messageType;
  }

  public static SocketResponse from(Message message) {
    return NoticeMessageResponse.builder()
        .notice(message.getContent())
        .messageType(MessageType.NOTICE)
        .build();
  }
}
