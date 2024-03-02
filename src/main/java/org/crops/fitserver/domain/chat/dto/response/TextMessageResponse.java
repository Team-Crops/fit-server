package org.crops.fitserver.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.global.socket.service.SocketResponse;

@Getter
public class TextMessageResponse extends SocketResponse {

  private Long userId;
  private String usermame;
  private String profileImageUrl;
  private String content;

  @Builder
  private TextMessageResponse(
      Long userId,
      String username,
      String profileImageUrl,
      String content,
      MessageType messageType) {
    this.userId = userId;
    this.usermame = username;
    this.profileImageUrl = profileImageUrl;
    this.content = content;
    this.messageType = messageType;
  }

  public static SocketResponse from(Message message) {
    return TextMessageResponse.builder()
        .userId(message.getUser().getId())
        .username(message.getUser().getUsername())
        .profileImageUrl(message.getUser().getProfileImageUrl())
        .content(message.getContent())
        .messageType(MessageType.TEXT)
        .build();
  }
}