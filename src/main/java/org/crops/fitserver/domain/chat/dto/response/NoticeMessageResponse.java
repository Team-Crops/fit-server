package org.crops.fitserver.domain.chat.dto.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.global.socket.service.SocketResponse;

@Getter
@RequiredArgsConstructor
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
