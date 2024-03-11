package org.crops.fitserver.domain.matching.dto;

import java.time.LocalDateTime;
import org.crops.fitserver.domain.matching.constant.MatchingStatus;
import org.crops.fitserver.domain.matching.entity.Matching;

public record MatchingDto(Long matchingId,
                          Long roomId,
                          Long positionId,
                          LocalDateTime createdAt,
                          LocalDateTime expiredAt,
                          MatchingStatus status) {

  public static MatchingDto from(Matching matching) {
    return new MatchingDto(
        matching.getId(),
        matching.getMatchingRoom() != null ? matching.getMatchingRoom().getId() : null,
        matching.getPosition().getId(),
        matching.getCreatedAt(),
        matching.getExpiredAt(),
        matching.getStatus()
    );
  }

}
