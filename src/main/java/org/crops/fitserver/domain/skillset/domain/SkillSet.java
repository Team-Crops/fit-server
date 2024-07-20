package org.crops.fitserver.domain.skillset.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.skillset.constant.PositionType;

@Entity
@Table(name = "skillset")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SkillSet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "skillset_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "skill_id", nullable = false)
  private Skill skill;

  @ManyToOne
  @JoinColumn(name = "position_id", nullable = false)
  private Position position;

  public static SkillSet create(Skill skill, Position position) {
    var skillSet = SkillSet.builder()
        .skill(skill)
        .position(position)
        .build();
    skill.addSkillSet(skillSet);
    position.addSkillSet(skillSet);
    return skillSet;
  }

  public boolean isEqualPositionType(PositionType positionType) {
    return this.position.getType().equals(positionType);
  }
}
