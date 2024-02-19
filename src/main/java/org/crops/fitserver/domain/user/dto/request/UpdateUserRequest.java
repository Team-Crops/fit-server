package org.crops.fitserver.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.domain.Link;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

  private JsonNullable<String> profileImageUrl;

  private JsonNullable<String> username;

  private JsonNullable<String> nickname;

  private JsonNullable<String> phoneNumber;

  @JsonProperty("isOpenPhoneNum")
  private JsonNullable<@NotNull Boolean> isOpenPhoneNum;

  private JsonNullable<String> email;

  private JsonNullable<BackgroundStatus> backgroundStatus;
  private JsonNullable<String> backgroundText;

  private JsonNullable<String> portfolioUrl;

  private JsonNullable<Integer> projectCount;

  private JsonNullable<Integer> activityHour;

  private JsonNullable<String> introduce;

  private JsonNullable<List<Link>> linkList;

  @JsonProperty("isOpenProfile")
  private JsonNullable<@NotNull Boolean> isOpenProfile;

  private JsonNullable<Long> positionId;

  private JsonNullable<Long> regionId;

  private JsonNullable<List<Long>> skillIdList;

  public JsonNullable<Boolean> getIsOpenPhoneNum() {
    return isOpenPhoneNum;
  }

  public void setIsOpenPhoneNum(Boolean isOpenPhoneNum) {
    this.isOpenPhoneNum = JsonNullable.of(isOpenPhoneNum);
  }

  public JsonNullable<Boolean> getIsOpenProfile() {
    return isOpenProfile;
  }

  public void setIsOpenProfile(Boolean isOpenProfile) {
    this.isOpenProfile = JsonNullable.of(isOpenProfile);
  }

  public static class UpdateUserRequestBuilder {

    public UpdateUserRequestBuilder profileImageUrl(String profileImageUrl) {
      this.profileImageUrl = JsonNullable.of(profileImageUrl);
      return this;
    }

    public UpdateUserRequestBuilder username(String username) {
      this.username = JsonNullable.of(username);
      return this;
    }

    public UpdateUserRequestBuilder nickname(String nickname) {
      this.nickname = JsonNullable.of(nickname);
      return this;
    }

    public UpdateUserRequestBuilder phoneNumber(String phoneNumber) {
      this.phoneNumber = JsonNullable.of(phoneNumber);
      return this;
    }

    public UpdateUserRequestBuilder email(String email) {
      this.email = JsonNullable.of(email);
      return this;
    }

    public UpdateUserRequestBuilder backgroundStatus(BackgroundStatus backgroundStatus) {
      this.backgroundStatus = JsonNullable.of(backgroundStatus);
      return this;
    }

    public UpdateUserRequestBuilder backgroundText(String backgroundText) {
      this.backgroundText = JsonNullable.of(backgroundText);
      return this;
    }

    public UpdateUserRequestBuilder portfolioUrl(String portfolioUrl) {
      this.portfolioUrl = JsonNullable.of(portfolioUrl);
      return this;
    }

    public UpdateUserRequestBuilder projectCount(Integer projectCount) {
      this.projectCount = JsonNullable.of(projectCount);
      return this;
    }

    public UpdateUserRequestBuilder activityHour(Integer activityHour) {
      this.activityHour = JsonNullable.of(activityHour);
      return this;
    }

    public UpdateUserRequestBuilder introduce(String introduce) {
      this.introduce = JsonNullable.of(introduce);
      return this;
    }

    public UpdateUserRequestBuilder linkList(List<Link> linkList) {
      this.linkList = JsonNullable.of(linkList);
      return this;
    }

    public UpdateUserRequestBuilder positionId(Long positionId) {
      this.positionId = JsonNullable.of(positionId);
      return this;
    }

    public UpdateUserRequestBuilder regionId(Long regionId) {
      this.regionId = JsonNullable.of(regionId);
      return this;
    }

    public UpdateUserRequestBuilder skillIdList(List<Long> skillIdList) {
      this.skillIdList = JsonNullable.of(skillIdList);
      return this;
    }

    public UpdateUserRequestBuilder isOpenPhoneNum(Boolean isOpenPhoneNum) {
      this.isOpenPhoneNum = JsonNullable.of(isOpenPhoneNum);
      return this;
    }

    public UpdateUserRequestBuilder isOpenProfile(Boolean isOpenProfile) {
      this.isOpenProfile = JsonNullable.of(isOpenProfile);
      return this;
    }
  }
}
