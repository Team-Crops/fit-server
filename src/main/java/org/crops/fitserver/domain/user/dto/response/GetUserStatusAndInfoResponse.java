package org.crops.fitserver.domain.user.dto.response;

import lombok.Builder;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.dto.UserProfileDto;

@Builder
public record GetUserStatusAndInfoResponse(
    boolean isLiked,
    UserProfileDto userProfile
) {

  public static GetUserStatusAndInfoResponse of(User user, boolean likeUser) {
    return GetUserStatusAndInfoResponse.builder()
        .userProfile(UserProfileDto.from(user))
        .isLiked(likeUser)
        .build();
  }
}
