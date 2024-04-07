package org.crops.fitserver.domain.matching.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.crops.fitserver.domain.matching.dto.MatchingUserView;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;

@Builder
public record GetMatchingRoomResponse(
    Long matchingRoomId,
    Long chatRoomId,
    Boolean isCompleted,
    LocalDateTime completedAt,
    Long hostUserId,
    List<MatchingUserView> matchingUserList
) {
  public static GetMatchingRoomResponse from(MatchingRoom matchingRoom) {
    return GetMatchingRoomResponse.builder()
        .matchingRoomId(matchingRoom.getId())
        .chatRoomId(matchingRoom.getChatRoomId())
        .isCompleted(matchingRoom.getIsCompleted())
        .completedAt(matchingRoom.getCompletedAt())
        .hostUserId(matchingRoom.getHostUserId())
        .matchingUserList(matchingRoom.getMatchingList().stream()
            .map(MatchingUserView::from)
            .toList())
        .build();
  }
}
