package org.crops.fitserver.domain.chat.dto.response;

public record MessageIdResponse(
    Long messageId
) {

  public static MessageIdResponse from(Long messageId) {
    return new MessageIdResponse(messageId);
  }
}
