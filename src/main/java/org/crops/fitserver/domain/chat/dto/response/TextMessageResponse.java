package org.crops.fitserver.domain.chat.dto.response;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Getter;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.global.socket.service.MessageResponse;

@Getter
public class TextMessageResponse extends MessageResponse {

  private Long userId;
  private String content;

  @Builder
  private TextMessageResponse(
      Long userId,
      String content,
      Long messageId,
      MessageType messageType,
      OffsetDateTime createdAt) {
    this.userId = userId;
    this.content = content;
    this.messageId = messageId;
    this.messageType = messageType;
    this.createdAt = createdAt;
  }

  public static TextMessageResponse from(Message message) {
    return TextMessageResponse.builder()
        .userId(message.getUser().getId())
        .content(message.getContent())
        .messageId(message.getId())
        .messageType(MessageType.TEXT)
        .createdAt(message.getCreatedAt())
        .build();
  }
}