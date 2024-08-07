package org.crops.fitserver.domain.matching.dto;

import java.time.OffsetDateTime;
import org.crops.fitserver.domain.matching.constant.MatchingStatus;
import org.crops.fitserver.domain.matching.entity.Matching;

public record MatchingDto(
    Long userId,
    Long roomId,
    Long positionId,
    OffsetDateTime createdAt,
    OffsetDateTime expiredAt,
    MatchingStatus status) {

  public static MatchingDto from(Matching matching) {
    return new MatchingDto(
        matching.getUser().getId(),
        matching.getMatchingRoom() != null ? matching.getMatchingRoom().getId() : null,
        matching.getPosition().getId(),
        matching.getCreatedAt(),
        matching.getExpiredAt(),
        matching.getStatus()
    );
  }

}
