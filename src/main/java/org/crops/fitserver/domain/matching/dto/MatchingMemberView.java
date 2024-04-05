package org.crops.fitserver.domain.matching.dto;

import org.crops.fitserver.domain.matching.entity.Matching;

public record MatchingMemberView(
    Long userId,
    Long positionId,
    String username,
    String profileImageUrl,
    Boolean isReady,
    Boolean isHost
) {

  public static MatchingMemberView from(Matching matching) {
    return new MatchingMemberView(
        matching.getUser().getId(),
        matching.getPosition().getId(),
        matching.getUser().getUsername(),
        matching.getUser().getProfileImageUrl(),
        matching.isReady(),
        matching.isHost()
    );
  }
}
