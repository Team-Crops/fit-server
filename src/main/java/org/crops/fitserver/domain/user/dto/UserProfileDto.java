package org.crops.fitserver.domain.user.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.domain.user.domain.User;

@Builder
public record UserProfileDto(
    long id,
    List<Link> linkList,
    String profileImageUrl,
    Long positionId,
    String nickname,
    String introduce,
    List<Long> skillIdList,
    UserInfoStatus status,
    BackgroundStatus backgroundStatus,
    String education,
    Integer projectCount,
    Long regionId,
    Short activityHour,
    String portfolioUrl,
    String email,
    String phoneNumber
) {

  public static UserProfileDto from(User user) {
    if (user == null) {
      return null;
    }
    UserProfileDtoBuilder userProfileDtoBuilder = UserProfileDto.builder();
    userProfileDtoBuilder.id(user.getId());
    userProfileDtoBuilder.profileImageUrl(user.getProfileImageUrl());
    if (user.getUserInfo().getPosition() != null) {
      userProfileDtoBuilder.positionId(user.getUserInfo().getPosition().getId());
    }
    userProfileDtoBuilder.nickname(user.getNickname());
    userProfileDtoBuilder.introduce(user.getUserInfo().getIntroduce());
    if (user.getUserInfo().getUserInfoSkills().size() > 0) {
      userProfileDtoBuilder.skillIdList(user
          .getUserInfo()
          .getUserInfoSkills()
          .stream()
          .map(userInfoSkill -> {
            if (userInfoSkill.getSkill() != null) {
              return userInfoSkill.getSkill().getId();
            }
            return null;
          })
          .collect(Collectors.toList()));
    }
    userProfileDtoBuilder.status(user.getUserInfo().getStatus());
    userProfileDtoBuilder.backgroundStatus(user.getUserInfo().getBackgroundStatus());
    userProfileDtoBuilder.education(user.getUserInfo().getEducation());
    userProfileDtoBuilder.projectCount(user.getUserInfo().getProjectCount());
    if (user.getUserInfo().getRegion() != null) {
      userProfileDtoBuilder.regionId(user.getUserInfo().getRegion().getId());
    }
    userProfileDtoBuilder.activityHour(user.getUserInfo().getActivityHour());
    userProfileDtoBuilder.portfolioUrl(user.getUserInfo().getPortfolioUrl());
    userProfileDtoBuilder.linkList(Link.parseToLinkList(user.getUserInfo().getLinkJson()));
    userProfileDtoBuilder.email(user.getEmail());
    userProfileDtoBuilder.phoneNumber(user.getPhoneNumber());
    return userProfileDtoBuilder.build();
  }
}
