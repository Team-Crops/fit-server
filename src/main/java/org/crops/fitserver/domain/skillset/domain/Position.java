package org.crops.fitserver.domain.skillset.domain;

import static jakarta.persistence.CascadeType.ALL;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.crops.fitserver.domain.skillset.constant.PositionType;
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

  @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<SkillSet> skillSets = new ArrayList<>();

  @Column(nullable = false)
  private String displayName;

  @Column(nullable = false)
  private String displayNameEn;

  @Column(nullable = false)
  private String imageUrl;

  @Column(nullable = true)//마이그레이션을 위해 null 허용
  @Enumerated(EnumType.STRING)
  private PositionType type;

  public void updateDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void updateDisplayNameEn(String displayNameEn) {
    this.displayNameEn = displayNameEn;
  }

  public void updateImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void addSkill(Skill skill) {
    if (this.skillSets.stream().anyMatch(skillSet -> skillSet.getSkill().equals(skill))) {
      return;
    }
    this.skillSets.add(SkillSet.create(skill, this));
  }

  public void removeSkill(Skill skill) {
    this.skillSets.removeIf(skillSet -> skillSet.getSkill().equals(skill));
  }
}
