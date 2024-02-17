package org.crops.fitserver.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.domain.Link;

@Builder
@Getter
public class UpdateUserRequest {

  private String profileImageUrl;

  private String username;

  private String nickname;

  private String phoneNumber;

  @JsonProperty("isOpenPhoneNum")
  @NotNull
  private Boolean isOpenPhoneNum;

  private String email;

  private BackgroundStatus backgroundStatus;
  private String backgroundText;

  private String portfolioUrl;

  private Integer projectCount;

  private Integer activityHour;

  private String introduce;

  private List<Link> linkList;

  @JsonProperty("isOpenProfile")
  @NotNull
  private Boolean isOpenProfile;

  private Long positionId;

  private Long regionId;

  private List<Long> skillIdList;

  public Boolean getIsOpenPhoneNum() {
    return isOpenPhoneNum;
  }

  public Boolean getIsOpenProfile() {
    return isOpenProfile;
  }

  public void setIsOpenPhoneNum(Boolean isOpenPhoneNum) {
    this.isOpenPhoneNum = isOpenPhoneNum;
  }

  public void setIsOpenProfile(Boolean isOpenProfile) {
    this.isOpenProfile = isOpenProfile;
  }
}
