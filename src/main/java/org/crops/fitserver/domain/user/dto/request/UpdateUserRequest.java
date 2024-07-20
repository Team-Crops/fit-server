package org.crops.fitserver.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.domain.Link;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record UpdateUserRequest(
    JsonNullable<String> profileImageUrl,
    JsonNullable<String> username,
    JsonNullable<String> nickname,
    JsonNullable<String> phoneNumber,
    JsonNullable<@NotNull Boolean> isOpenPhoneNum,
    JsonNullable<String> email,
    JsonNullable<BackgroundStatus> backgroundStatus,
    JsonNullable<String> backgroundText,
    JsonNullable<String> portfolioUrl,
    JsonNullable<Integer> projectCount,
    JsonNullable<Short> activityHour,
    JsonNullable<String> introduce,
    JsonNullable<List<Link>> linkList,
    JsonNullable<@NotNull Boolean> isOpenProfile,
    JsonNullable<Long> positionId,
    JsonNullable<Long> regionId,
    JsonNullable<List<Long>> skillIdList

) {

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

    public UpdateUserRequestBuilder activityHour(short activityHour) {
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
