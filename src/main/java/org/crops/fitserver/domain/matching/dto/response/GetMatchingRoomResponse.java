package org.crops.fitserver.domain.matching.dto.response;

import java.time.LocalDateTime;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;

public record GetMatchingRoomResponse(
    Long matchingRoomId,
    Long chatRoomId,
    Boolean isComplete,
    LocalDateTime completedAt,
    Long hostUserId
) {

  public static GetMatchingRoomResponse from(MatchingRoom matchingRoom) {
    return new GetMatchingRoomResponse(
        matchingRoom.getId(),
        matchingRoom.getChatRoomId(),
        matchingRoom.getIsComplete(),
        matchingRoom.getCompletedAt(),
        matchingRoom.getHostUserId()
    );
  }
}
