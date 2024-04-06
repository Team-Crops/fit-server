package org.crops.fitserver.domain.recommend.dto;

import lombok.Builder;
import org.crops.fitserver.domain.user.domain.User;

@Builder
public record RecommendUserDto(
    UserSummaryDto userSummary,
    Boolean isLiked
) {

  public static RecommendUserDto of(User user, boolean isLiked) {
    return RecommendUserDto.builder()
        .userSummary(UserSummaryDto.from(user))
        .isLiked(isLiked)
        .build();
  }
}
