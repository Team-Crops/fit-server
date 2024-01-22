package org.crops.fitserver.domain.user.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.skillset.domain.Skill;

@Entity(name = "user_info_skill")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoSkill {

  @Id
  @GeneratedValue
  @Column(name = "user_info_skill_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_info_id", nullable = false)
  private UserInfo userInfo;

  @ManyToOne
  @JoinColumn(name = "skill_id", nullable = false)
  private Skill skill;
}
