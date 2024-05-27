package org.crops.fitserver.domain.alarm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE alarm SET is_deleted = true WHERE alarm_id = ?")
public class Alarm extends BaseTimeEntity {

  @Id
  @Column(name = "alarm_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 50)
  private AlarmCase alarmCase;

  @Column(nullable = false)
  @ColumnDefault("false")
  private boolean isRead;

  public static Alarm create(User user, AlarmCase alarmCase) {
    return Alarm.builder()
        .user(user)
        .alarmCase(alarmCase)
        .isRead(false)
        .build();
  }
}
