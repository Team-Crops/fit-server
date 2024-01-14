package org.crops.fitserver.domain.chat.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.global.socket.service.SocketResponse;

@Getter
public class ImageMessageResponse extends SocketResponse {

  private Long userId;
  private String userName;
  private String profileImageUrl;
  private String imagesUrl;

  @Builder
  private ImageMessageResponse(
      Long userId,
      String userName,
      String profileImageUrl,
      String imagesUrl,
      MessageType messageType) {
    this.userId = userId;
    this.userName = userName;
    this.profileImageUrl = profileImageUrl;
    this.imagesUrl = imagesUrl;
    this.messageType = messageType;
  }

  public static SocketResponse from(Message message) {
    return ImageMessageResponse.builder()
        .userId(message.getUser().getId())
        .userName(message.getUser().getUserName())
        .profileImageUrl(message.getUser().getProfileImageUrl())
        .imagesUrl(message.getContent())
        .messageType(MessageType.IMAGE)
        .build();
  }
}
