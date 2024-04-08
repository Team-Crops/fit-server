package org.crops.fitserver.domain.matching.dto;

import org.crops.fitserver.domain.matching.entity.Matching;

public record MatchingUserView(
    Long userId,
    Long positionId,
    String username,
    String profileImageUrl,
    Boolean isReady,
    Boolean isHost
) {

  public static MatchingUserView from(Matching matching) {
    return new MatchingUserView(
        matching.getUser().getId(),
        matching.getPosition().getId(),
        matching.getUser().getUsername(),
        matching.getUser().getProfileImageUrl(),
        matching.isReady(),
        matching.isHost()
    );
  }
}
