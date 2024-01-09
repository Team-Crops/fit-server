package org.crops.fitserver.global.socket.service;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.domain.MessageType;

@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SocketResponse<T> {

  private final Long userId;
  private final String userName;
  private final String profileImageUrl;
  private final MessageType messageType;
  private final T content;

  public static <T> SocketResponse<T> of(
      Long userId,
      String userName,
      String profileImageUrl,
      MessageType messageType,
      T content) {
    return SocketResponse.<T>builder()
        .userId(userId)
        .userName(userName)
        .profileImageUrl(profileImageUrl)
        .messageType(messageType)
        .content(content)
        .build();
  }
}
