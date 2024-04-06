package org.crops.fitserver.domain.recommend.dto;

import java.util.List;
import lombok.Builder;
import org.crops.fitserver.domain.user.domain.User;

@Builder
public record UserSummaryDto(
    Long userId,
    String username,
    String positionName,
    String introduce,
    String profileImageUrl,
    List<Long> skillIdList
) {
  public static UserSummaryDto from(User user) {
    return UserSummaryDto.builder()
        .userId(user.getId())
        .username(user.getUsername())
        .profileImageUrl(user.getProfileImageUrl())
        .positionName(user.getUserInfo().getPosition().getDisplayName())
        .introduce(user.getUserInfo().getIntroduce())
        .skillIdList(user
            .getUserInfo()
            .getUserInfoSkills()
            .stream()
            .map(userInfoSkill ->
                    userInfoSkill.getSkill().getId())
            .toList())
        .build();
  }
}