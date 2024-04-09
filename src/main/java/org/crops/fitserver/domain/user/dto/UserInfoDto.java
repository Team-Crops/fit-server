package org.crops.fitserver.domain.user.dto;


import static org.crops.fitserver.domain.user.domain.Link.parseToLinkList;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.constant.BackgroundStatus.BackgroundType;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.domain.user.domain.User;
import org.springframework.util.CollectionUtils;

@Getter
@Builder
public class UserInfoDto {

  private long id;
  private String profileImageUrl;
  private String username;
  private String nickname;
  private String phoneNumber;
  @JsonProperty("isOpenPhoneNum")
  private Boolean isOpenPhoneNum;
  private String email;
  private BackgroundStatus backgroundStatus;
  private String backgroundText;
  @JsonProperty("isOpenProfile")
  private Boolean isOpenProfile;
  private String portfolioUrl;
  private Integer projectCount;
  private Short activityHour;
  private String introduce;
  private List<Link> linkList;
  private Long positionId;
  private Long regionId;
  private List<Long> skillIdList;

  private UserInfoStatus status;


  public static UserInfoDto from(User user) {
    if (user == null) {
      return UserInfoDto.builder().build();
    }

    var userInfoDtoBuilder = UserInfoDto.builder()
        .id(user.getId())
        .profileImageUrl(user.getProfileImageUrl())
        .username(user.getUsername())
        .nickname(user.getNickname())
        .phoneNumber(user.getPhoneNumber())
        .isOpenPhoneNum(user.isOpenPhoneNum())
        .email(user.getEmail())
        .backgroundStatus(user.getUserInfo().getBackgroundStatus());

    if (user.getUserInfo() != null) {
      userInfoDtoBuilder = userInfoDtoBuilder
          .portfolioUrl(user.getUserInfo().getPortfolioUrl())
          .projectCount(user.getUserInfo().getProjectCount())
          .activityHour(user.getUserInfo().getActivityHour())
          .introduce(user.getUserInfo().getIntroduce())
          .linkList(parseToLinkList(user.getUserInfo().getLinkJson()))
          .isOpenProfile(user.getUserInfo().isOpenProfile())
          .status(user.getUserInfo().getStatus());

      if (user.getUserInfo().getBackgroundStatus() != null) {
        userInfoDtoBuilder = userInfoDtoBuilder
            .backgroundText(
                user.getUserInfo().getBackgroundStatus().getBackgroundType()
                    == BackgroundType.CAREER ? user.getUserInfo().getCareer()
                    : user.getUserInfo().getEducation()
            );
      }

      if (user.getUserInfo().getPosition() != null) {
        userInfoDtoBuilder = userInfoDtoBuilder.positionId(
            user.getUserInfo().getPosition().getId());
      }

      if (user.getUserInfo().getRegion() != null) {
        userInfoDtoBuilder = userInfoDtoBuilder.regionId(user.getUserInfo().getRegion().getId());
      }

      if (!CollectionUtils.isEmpty(user.getUserInfo().getUserInfoSkills())) {
        userInfoDtoBuilder = userInfoDtoBuilder.skillIdList(
            user.getUserInfo().getUserInfoSkills().stream()
                .map(userInfoSkill -> userInfoSkill.getSkill().getId()).toList());
      }
    }

    return userInfoDtoBuilder.build();
  }
}
