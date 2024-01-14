package org.crops.fitserver.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateUserRequest {

  private String profileImageUrl;

  private String username;

  private String nickname;

  private String phoneNumber;

  private boolean isOpenPhoneNum;

  private String email;

  private String career;

  private String portfolioUrl;

  private Integer projectCount;

  private Integer activityHour;

  private String introduce;

  private String linkJson;

  private boolean isOpenProfile;

  private Long positionId;

  private Long regionId;
}
