package org.crops.fitserver.domain.school.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.school.constant.SchoolType;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@DynamicInsert
@SQLDelete(sql = "UPDATE school SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class School extends BaseTimeEntity {
  @Id
  @GeneratedValue
  @Column(name = "school_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column()
  @Enumerated(value = EnumType.STRING)
  private SchoolType type;

}
