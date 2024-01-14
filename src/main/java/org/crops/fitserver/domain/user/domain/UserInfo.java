package org.crops.fitserver.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.util.CollectionUtils;

@Entity(name = "user_info")
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfo {

  @Id
  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
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
  private Boolean isOpenProfile;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false, length = 10)
  @ColumnDefault("'INCOMPLETE'")
  @Setter(AccessLevel.PRIVATE)
  private UserInfoStatus status;

  @OneToMany(mappedBy = "userInfo")
  private final List<UserInfoSkill> userInfoSkills = new ArrayList<>();

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
        .user(user)
        .isOpenProfile(false)
        .status(UserInfoStatus.INCOMPLETE)
        .build();
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
        && this.user.getNickName() != null
        && this.user.getPhoneNumber() != null
        ;
  }
}
