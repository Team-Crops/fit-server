package org.crops.fitserver.domain.matching.dto.response;

import org.crops.fitserver.domain.matching.entity.MatchingRoom;

public record GetMatchingRoomResponse(
    Long id,
    Long chatRoomId,
    Boolean isMatched,
    Long matchedAt,
    Long hostUserId
) {

  public static GetMatchingRoomResponse from(MatchingRoom matchingRoom) {
    return new GetMatchingRoomResponse(
        matchingRoom.getId(),
        matchingRoom.getChatRoomId(),
        matchingRoom.getIsMatched(),
        matchingRoom.getMatchedAt(),
        matchingRoom.getHostUserId()
    );
  }
}
