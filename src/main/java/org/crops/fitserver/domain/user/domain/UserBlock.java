package org.crops.fitserver.domain.user.domain;


import static org.crops.fitserver.domain.user.constant.BlockConstants.DEFAULT_BLOCK_DAYS;

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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.crops.fitserver.domain.user.constant.BlockConstants.BlockStatus;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class UserBlock extends BaseTimeEntity {
  @Id
  @Column(name = "user_block_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private LocalDateTime blockedAt;

  @Column(nullable = true)
  private LocalDateTime unblockedAt;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private BlockStatus blockStatus;

  public static UserBlock create(User user) {
    return UserBlock.builder()
        .user(user)
        .blockedAt(LocalDateTime.now())
        .unblockedAt(LocalDateTime.now().plusDays(DEFAULT_BLOCK_DAYS))
        .blockStatus(BlockStatus.BLOCKED)
        .build();
  }

  public void extendBlock() {
    this.unblockedAt = LocalDateTime.now().plusDays(DEFAULT_BLOCK_DAYS);
  }
}
