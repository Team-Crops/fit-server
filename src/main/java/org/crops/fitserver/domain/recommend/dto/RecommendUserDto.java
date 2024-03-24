package org.crops.fitserver.domain.recommend.dto;

import java.util.List;
import lombok.Builder;
import org.crops.fitserver.domain.user.domain.User;

@Builder
public record RecommendUserDto(
    Long userId,
    String username,
    String positionName,
    String introduce,
    String portfolioUrl,
    List<String> skill,
    Boolean isLiked
) {

  public static RecommendUserDto of(User user) {
    return RecommendUserDto.builder()
        .userId(user.getId())
        .username(user.getUsername())
        .positionName(user.getUserInfo().getPosition().getDisplayName())
        .introduce(user.getUserInfo().getIntroduce())
        .portfolioUrl(user.getUserInfo().getPortfolioUrl())
        .skill(user
            .getUserInfo()
            .getUserInfoSkills()
            .stream()
            .map(userInfoSkill ->
                    userInfoSkill.getSkill().getDisplayName())
            .toList())
        .build();
  }
}
