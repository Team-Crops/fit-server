package org.crops.fitserver.domain.skillset.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@SQLDelete(sql = "UPDATE position SET is_deleted = true WHERE position_id = ?")
@Where(clause = "is_deleted = false")
public class Position extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "position_id")
  private Long id;

  @OneToMany(mappedBy = "position")
  private final List<SkillSet> skillSets = new ArrayList<>();

  @Column(nullable = false)
  private String displayName;

  public void updateDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void addSkill(Skill skill) {
    this.skillSets.add(SkillSet.create(skill, this));
  }

  public void removeSkill(Skill skill) {
    this.skillSets.removeIf(skillSet -> skillSet.getSkill().equals(skill));
  }
}
