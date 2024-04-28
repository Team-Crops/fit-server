package org.crops.fitserver.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.global.socket.service.MessageResponse;

@Getter
public class ImageMessageResponse extends MessageResponse {

  private Long userId;
  private String imagesUrl;

  @Builder
  private ImageMessageResponse(
      Long userId,
      String imagesUrl,
      Long messageId,
      MessageType messageType) {
    this.userId = userId;
    this.imagesUrl = imagesUrl;
    this.messageId = messageId;
    this.messageType = messageType;
  }

  public static MessageResponse from(Message message) {
    return ImageMessageResponse.builder()
        .userId(message.getUser().getId())
        .imagesUrl(message.getContent())
        .messageId(message.getId())
        .messageType(MessageType.IMAGE)
        .build();
  }
}
