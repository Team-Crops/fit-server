package org.crops.fitserver.domain.chat.dto.response;

public record GetLastSeenMessageResponse(
    Long messageId
) {

  public static GetLastSeenMessageResponse from(Long messageId) {
    return new GetLastSeenMessageResponse(messageId);
  }
}
