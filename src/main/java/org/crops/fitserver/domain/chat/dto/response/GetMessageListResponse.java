package org.crops.fitserver.domain.chat.dto.response;

import org.crops.fitserver.global.http.PageResult;
import org.crops.fitserver.global.socket.service.MessageResponse;

public record GetMessageListResponse(
    PageResult<MessageResponse> pageResult
) {

  public static GetMessageListResponse from(PageResult<MessageResponse> pageResult) {
    return new GetMessageListResponse(pageResult);
  }
}
