package org.crops.fitserver.domain.user.domain;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.util.LinkUtil;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.util.CollectionUtils;

@Entity(name = "user_info")
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class UserInfo {

  @OneToMany(mappedBy = "userInfo")
  private final List<UserInfoSkill> userInfoSkills = new ArrayList<>();
  @Id
  @Column(name = "user_id")
  private Long id;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", nullable = false)
  @MapsId
  private User user;
  @Column(length = 2048)
  private String portfolioUrl;
  private Integer projectCount;
  private Integer activityHour;
  @Column(length = 255)
  private String introduce;
  @Column(length = 2048)
  private String linkJson;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 10)
  private BackgroundStatus backgroundStatus;

  private String career;
  private String education;

  @Column(nullable = false)
  @ColumnDefault("false")
  @Setter(AccessLevel.PRIVATE)
  private boolean isOpenProfile;
  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false, length = 10)
  @ColumnDefault("'INCOMPLETE'")
  @Setter(AccessLevel.PRIVATE)
  private UserInfoStatus status;
  /**
   * cascade = CascadeType.ALL 을 사용하면 id만으로 entity 교체할 때 문제가 발생한다.
   */
  @ManyToOne
  @JoinColumn(name = "position_id")
  private Position position;

  @ManyToOne
  @JoinColumn(name = "region_id")
  private Region region;

  public static UserInfo from(User user) {
    return UserInfo.builder()
        .id(user.getId())
        .user(user)
        .isOpenProfile(false)
        .status(UserInfoStatus.INCOMPLETE)
        .build();
  }

  @PrePersist
  public void prePersist() {
    this.status = this.isReadyToComplete() ? UserInfoStatus.COMPLETE : UserInfoStatus.INCOMPLETE;
  }

  @PreUpdate
  public void preUpdate() {
    this.status = this.isReadyToComplete() ? UserInfoStatus.COMPLETE : UserInfoStatus.INCOMPLETE;
  }

  public void updateUserInfo(UserInfo userInfo) {
    this.updatePortfolioUrl(userInfo.getPortfolioUrl());
    this.updateProjectCount(userInfo.getProjectCount());
    this.updateActivityHour(userInfo.getActivityHour());
    this.updateIntroduce(userInfo.getIntroduce());
    this.updateLinkJson(userInfo.getLinkJson());
    this.updateIsOpenProfile(userInfo.isOpenProfile());
    this.updatePosition(userInfo.getPosition());
    this.updateRegion(userInfo.getRegion());
  }

  public void updateUserInfo(UpdateUserRequest updateUserRequest) {
    this.updatePortfolioUrl(updateUserRequest.getPortfolioUrl());
    this.updateProjectCount(updateUserRequest.getProjectCount());
    this.updateActivityHour(updateUserRequest.getActivityHour());
    this.updateIntroduce(updateUserRequest.getIntroduce());
    this.updateLinkJson(LinkUtil.parseToJson(updateUserRequest.getLinkList()));
    this.updateBackground(updateUserRequest.getBackgroundStatus(),
        updateUserRequest.getBackgroundText());
    this.updateIsOpenProfile(updateUserRequest.getIsOpenProfile());
    this.updatePosition(updateUserRequest.getPositionId());
    this.updateRegion(updateUserRequest.getRegionId());
  }


  public void updatePortfolioUrl(String portfolioUrl) {
    this.portfolioUrl = portfolioUrl;
  }

  public void updateProjectCount(Integer projectCount) {
    if (this.projectCount != null && projectCount == null) {
      throw new IllegalArgumentException("projectCount cannot be null");
    }
    this.projectCount = projectCount;
  }

  public void updateActivityHour(Integer activityHour) {
    if (this.activityHour != null && activityHour == null) {
      throw new IllegalArgumentException("activityHour cannot be null");
    }
    this.activityHour = activityHour;
  }

  public void updateIntroduce(String introduce) {
    if (StringUtils.isNotBlank(this.introduce) && StringUtils.isBlank(introduce)) {
      throw new IllegalArgumentException("introduce cannot be null");
    }
    this.introduce = introduce;
  }

  public void updateLinkJson(String linkJson) {
    if (StringUtils.isNotBlank(this.linkJson) && StringUtils.isBlank(linkJson)) {
      throw new IllegalArgumentException("linkJson cannot be null");
    }
    this.linkJson = linkJson;
  }

  public void updateBackground(BackgroundStatus backgroundStatus, String backgroundText) {
    if(this.backgroundStatus != null && backgroundStatus == null) {
      throw new IllegalArgumentException("backgroundStatus cannot be null");
    }
    this.backgroundStatus = backgroundStatus;

    if(this.backgroundStatus == null) {
      this.career = null;
      this.education = null;
      return;
    }

    switch (backgroundStatus.getBackgroundType()) {
      case CAREER -> {
        this.career = backgroundText;
        this.education = null;
      }
      case EDUCATION -> {
        this.career = null;
        this.education = backgroundText;
      }
    }

  }

  public void updateIsOpenProfile(boolean isOpenProfile) {
    this.isOpenProfile = isOpenProfile;
  }

  private void updatePosition(Long positionId) {
    updatePosition(positionId != null ? Position.builder()
        .id(positionId).build() : null);
  }


  public void updatePosition(Position position) {
    if (this.position != null && position == null) {
      throw new IllegalArgumentException("position cannot be null");
    }
    this.position = position;
  }

  private void updateRegion(Long regionId) {
    updateRegion(regionId != null ? Region.builder()
        .id(regionId).build() : null);
  }

  public void updateRegion(Region region) {
    if (this.region != null && region == null) {
      throw new IllegalArgumentException("region cannot be null");
    }
    this.region = region;
  }

  public void addSkill(Skill skill) {
    this.userInfoSkills.add(UserInfoSkill.builder()
        .userInfo(this)
        .skill(skill)
        .build());
  }

  public void removeSkill(Skill skill) {
    this.userInfoSkills.removeIf(userInfoSkill -> userInfoSkill.getSkill().equals(skill));
  }

  private boolean isReadyToComplete() {
    return this.projectCount != null
        && this.activityHour != null
        && this.introduce != null
        && this.linkJson != null
        && !CollectionUtils.isEmpty(this.userInfoSkills)
        && this.position != null
        && this.region != null
        && this.backgroundStatus != null
        && this.user.getEmail() != null
        && this.user.getNickname() != null
        && this.user.getPhoneNumber() != null
        ;
  }
}
