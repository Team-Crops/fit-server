package org.crops.fitserver.domain.chat.dto.response;

import org.crops.fitserver.domain.chat.dto.MessageDto;
import org.crops.fitserver.global.http.CursorResult;

public record GetMessageListResponse(
    CursorResult<MessageDto> cursorResult
) {

  public static GetMessageListResponse of(CursorResult<MessageDto> cursorResult) {
    return new GetMessageListResponse(cursorResult);
  }
}
