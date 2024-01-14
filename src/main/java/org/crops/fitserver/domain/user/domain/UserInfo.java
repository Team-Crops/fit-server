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
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
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

  @Column(nullable = false)
  @ColumnDefault("false")
  @Setter(AccessLevel.PRIVATE)
  private boolean isOpenProfile;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false, length = 10)
  @ColumnDefault("'INCOMPLETE'")
  @Setter(AccessLevel.PRIVATE)
  private UserInfoStatus status;


  @OneToMany(mappedBy = "userInfo")
  private final List<UserInfoSkill> userInfoSkills = new ArrayList<>();

  /**
   * cascade = CascadeType.ALL 을 사용하면 id만으로 entity 교체할 때 문제가 발생한다.
   */
  @ManyToOne
  @JoinColumn(name = "position_id")
  private Position position;

  @ManyToOne
  @JoinColumn(name = "region_id")
  private Region region;


  @PreUpdate
  public void preUpdate() {
    this.status = this.isReadyToComplete() ? UserInfoStatus.COMPLETE : UserInfoStatus.INCOMPLETE;
  }

  public static UserInfo from(User user) {
    return UserInfo.builder()
        .id(user.getId())
        .user(user)
        .isOpenProfile(false)
        .status(UserInfoStatus.INCOMPLETE)
        .build();
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
    this.updateLinkJson(updateUserRequest.getLinkJson());
    this.updateIsOpenProfile(updateUserRequest.isOpenProfile());

    var position = updateUserRequest.getPositionId() != null ? Position.builder()
        .id(updateUserRequest.getPositionId()).build() : null;
    this.updatePosition(position);

    var region = updateUserRequest.getRegionId() != null ? Region.builder()
        .id(updateUserRequest.getRegionId()).build() : null;
    this.updateRegion(region);
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

  public void updateIsOpenProfile(boolean isOpenProfile) {
    this.isOpenProfile = isOpenProfile;
  }

  public void updatePosition(Position position) {
    if (this.position != null && position == null) {
      throw new IllegalArgumentException("position cannot be null");
    }
    this.position = position;
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
        && this.user.getCareer() != null
        && this.user.getEmail() != null
        && this.user.getNickname() != null
        && this.user.getPhoneNumber() != null
        ;
  }
}
