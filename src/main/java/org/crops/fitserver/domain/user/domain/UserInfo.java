package org.crops.fitserver.domain.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.hibernate.annotations.DynamicInsert;

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

  private String portfolioUrl;

  private Integer activityHour;

  private String introduce;

  private String linkJson;



  @ManyToMany(targetEntity = Skill.class)
  @JoinTable(name = "user_info_skill", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
  private List<Skill> skills;

  @ManyToOne
  @JoinColumn(name = "position_id")
  private Position position;

  @ManyToOne
  @JoinColumn(name = "region_id")
  private Region region;
}
