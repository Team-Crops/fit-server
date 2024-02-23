package org.crops.fitserver.domain.region.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@DynamicInsert
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE region SET is_deleted = true WHERE region_id = ?")
@Where(clause = "is_deleted = false")
public class Region extends BaseTimeEntity {

  @Id
  @Column(name = "region_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String displayName;

  public void updateDisplayName(String newDisplayName) {
    this.displayName = newDisplayName;
  }
}
