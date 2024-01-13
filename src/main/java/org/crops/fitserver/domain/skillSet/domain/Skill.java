package org.crops.fitserver.domain.skillSet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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
}
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@SQLDelete(sql = "UPDATE skill SET is_deleted = true WHERE skill_id = ?")
@Where(clause = "is_deleted = false")
public class Skill extends BaseTimeEntity {

  @Id
  @GeneratedValue
  @Column(name = "skill_id")
  private Long id;

  @Column(name = "display_name", length = 50, nullable = false, unique = true)
  private String displayName;

  public void updateDisplayName(String displayName) {
    this.displayName = displayName;
  }
}
