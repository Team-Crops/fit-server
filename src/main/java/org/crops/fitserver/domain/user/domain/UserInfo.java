package org.crops.fitserver.domain.user.domain;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.openapitools.jackson.nullable.JsonNullable;

@Entity(name = "user_info")
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE user_info SET is_deleted = true WHERE user_id = ?")
public class UserInfo extends BaseTimeEntity {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  @MapsId
  private User user;
  @Column(length = 2048)
  private String portfolioUrl;
  private Integer projectCount;
  private Short activityHour;
  @Column(length = 255)
  private String introduce;
  @Column(length = 2048)
  private String linkJson;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 20)
  private BackgroundStatus backgroundStatus;

  private String career;
  private String education;

  @Column(nullable = false)
  @ColumnDefault("false")
  private boolean isOpenProfile;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false, length = 10)
  @ColumnDefault("'INCOMPLETE'")
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

  @BatchSize(size = 50)
  @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<UserInfoSkill> userInfoSkills = new ArrayList<>();

  /**
   * User.prePersist() 에서 호출. 다른 곳에서 절대로 호출해서는 안됨.
   */
  public static UserInfo from(User user) {
    return UserInfo.builder()
        .id(user.getId())
        .user(user)
        .isOpenProfile(false)
        .status(UserInfoStatus.INCOMPLETE)
        .build();
  }

  public void updateStatus(UserInfoStatus status) {
    this.status = status;
  }


  public UserInfo withPortfolioUrl(String portfolioUrl) {
    this.portfolioUrl = portfolioUrl;
    return this;
  }

  public UserInfo withProjectCount(Integer projectCount) {
    if (this.projectCount != null && projectCount == null) {
      throw new IllegalArgumentException("projectCount cannot be null");
    }
    this.projectCount = projectCount;
    return this;
  }

  public UserInfo withActivityHour(Short activityHour) {
    if (this.activityHour != null && activityHour == null) {
      throw new IllegalArgumentException("activityHour cannot be null");
    }
    this.activityHour = activityHour;
    return this;
  }

  public UserInfo withIntroduce(String introduce) {
    this.introduce = introduce;
    return this;
  }

  public UserInfo withLinkJson(String linkJson) {
    this.linkJson = linkJson;
    return this;
  }

  public UserInfo withBackground(JsonNullable<BackgroundStatus> backgroundStatus,
      JsonNullable<String> backgroundText) {
    if (!backgroundStatus.isPresent() && !backgroundText.isPresent()) {
      return this;
    }
    if (this.backgroundStatus == null && !backgroundStatus.isPresent()) {
      return this;//둘 다 널이면 무시
    }

    withBackground(
        backgroundStatus.orElse(this.backgroundStatus),
        backgroundStatus.get() == this.backgroundStatus
            ? backgroundText.orElse(getBackgroundText())
            : null);

    return this;
  }

  private String getBackgroundText() {
    return this.backgroundStatus.getBackgroundType() == BackgroundStatus.BackgroundType.CAREER
        ? this.career : this.education;
  }

  public UserInfo withBackground(BackgroundStatus backgroundStatus, String backgroundText) {
    if (this.backgroundStatus != null && backgroundStatus == null) {
      throw new IllegalArgumentException("backgroundStatus cannot be null");
    }
    this.backgroundStatus = backgroundStatus;

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
    return this;
  }

  public UserInfo withIsOpenProfile(boolean isOpenProfile) {
    this.isOpenProfile = isOpenProfile;
    return this;
  }

  public UserInfo withPosition(Position position) {
    if (this.position != null && position == null) {
      throw new IllegalArgumentException("position cannot be null");
    }
    this.position = position;
    return this;
  }

  public UserInfo withRegion(Region region) {
    if (this.region != null && region == null) {
      throw new IllegalArgumentException("region cannot be null");
    }
    this.region = region;

    return this;
  }

  public void addSkill(Skill skill) {
    this.userInfoSkills.add(UserInfoSkill.builder()
        .userInfo(this)
        .skill(skill)
        .build());
  }

  public UserInfo withSkills(List<Skill> skillList) {
    skillList.stream().filter(
        skill -> this.userInfoSkills.stream()
            .noneMatch(userInfoSkill -> userInfoSkill.getSkill().equals(skill))
    ).forEach(this::addSkill);
    this.userInfoSkills.removeIf(userInfoSkill -> skillList.stream()
        .noneMatch(skill -> userInfoSkill.getSkill().equals(skill)));

    return this;
  }

  public void removeSkill(Skill skill) {
    this.userInfoSkills.removeIf(userInfoSkill -> userInfoSkill.getSkill().equals(skill));
  }

  protected void setUser(User user) {
    if (this.user != null && this.user.getUserInfo() != this) {
      throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
    this.user = user;
  }


  public void withdraw() {
    this.status = UserInfoStatus.WITHDRAWAL;
    this.portfolioUrl = null;
    this.projectCount = null;
    this.activityHour = null;
    this.introduce = null;
    this.linkJson = null;
    this.backgroundStatus = null;
    this.career = null;
    this.education = null;
    this.isOpenProfile = false;
    this.position = null;
    this.region = null;
    this.isDeleted = true;
    this.userInfoSkills.clear();
  }
}
