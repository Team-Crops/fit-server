package org.crops.fitserver.domain.chat.dto.response;

import java.util.List;
import org.crops.fitserver.domain.chat.dto.MessageDto;

public record GetMessageListResponse(
    List<MessageDto> messages
) {

  public static GetMessageListResponse of(List<MessageDto> messages) {
    return new GetMessageListResponse(messages);
  }
}
