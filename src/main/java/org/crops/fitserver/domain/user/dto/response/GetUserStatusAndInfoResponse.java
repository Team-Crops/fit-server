package org.crops.fitserver.domain.user.dto.response;

import lombok.Builder;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.dto.UserProfileDto;

@Builder
public record GetUserStatusAndInfoResponse(
    boolean isLiked,
    UserProfileDto userProfile
) {

  public static GetUserStatusAndInfoResponse of(User userWithInfo, boolean likeUser) {
    return GetUserStatusAndInfoResponse.builder()
        .userProfile(UserProfileDto.from(userWithInfo))
        .isLiked(likeUser)
        .build();
  }
}
