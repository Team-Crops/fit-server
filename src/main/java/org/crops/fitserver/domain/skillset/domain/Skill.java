package org.crops.fitserver.domain.skillset.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(indexes = {
    @Index(name = "skill_display_name_idx", columnList = "display_name")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@SQLDelete(sql = "UPDATE skill SET is_deleted = true WHERE skill_id = ?")
@Where(clause = "is_deleted = false")
public class Skill extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "skill_id")
  private Long id;

  @Column(name = "display_name", length = 50, nullable = false, unique = true)
  private String displayName;

  @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<SkillSet> skillSets = new ArrayList<>();

  public void updateDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public List<Position> getPositions() {
    List<Position> positions = new ArrayList<>();
    this.skillSets.forEach(skillSet -> positions.add(skillSet.getPosition()));
    return positions;
  }


  public void addPosition(Position position) {
    if (this.skillSets.stream().anyMatch(skillSet -> skillSet.getPosition().equals(position))) {
      return;
    }
    this.skillSets.add(SkillSet.create(this, position));
  }

  protected void addSkillSet(SkillSet skillSet) {
    this.skillSets.add(skillSet);
  }

  public void removePosition(Position position) {
    this.skillSets.removeIf(skillSet -> skillSet.getPosition().equals(position));
  }
}
