package org.crops.fitserver.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreUpdate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
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

  @Column(length = 255)
  private String portfolioUrl;

  private Integer projectCount;

  private Integer activityHour;

  @Column(length = 255)
  private String introduce;

  @Column(length = 2048)
  private String linkJson;

  @Column(nullable = false)
  private boolean isOpenProfile = false;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false, length = 10)
  private UserInfoStatus status = UserInfoStatus.INCOMPLETE;


  @ManyToMany(targetEntity = Skill.class)
  @JoinTable(name = "user_info_skill", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
  private List<Skill> skills;

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
        .build();
  }

  private boolean isReadyToComplete() {
    return this.projectCount != null
        && this.activityHour != null
        && this.introduce != null
        && this.linkJson != null
        && !CollectionUtils.isEmpty(this.skills)
        && this.position != null
        && this.region != null
        && this.user.getCareer() != null
        && this.user.getEmail() != null
        && this.user.getNickName() != null
        && this.user.getPhoneNumber() != null
        ;
  }
}
