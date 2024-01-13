package org.crops.fitserver.domain.skillSet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.UniqueConstraint;
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

  @ManyToMany
  @JoinTable(name = "skillset",
      joinColumns = @JoinColumn(name = "position_id"),
      inverseJoinColumns = @JoinColumn(name = "skill_id"),
      uniqueConstraints = @UniqueConstraint(columnNames = {"position_id", "skill_id"})
  )
  private final List<Skill> skills = new ArrayList<>();
  @Id
  @GeneratedValue
  @Column(name = "position_id")
  private Long id;
  @Column(nullable = false)
  private String displayName;

  public void updateDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void addSkill(Skill skill) {
    this.skills.add(skill);
  }

  public void removeSkill(Skill skill) {
    this.skills.remove(skill);
  }
}
