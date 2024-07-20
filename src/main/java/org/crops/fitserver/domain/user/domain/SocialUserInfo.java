package org.crops.fitserver.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE social_user_info SET is_deleted = true WHERE social_user_info_id = ?")
public class SocialUserInfo extends BaseTimeEntity {

  @Id
  @Column(name = "social_user_info_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false, length = 10)
  private SocialPlatform socialType;

  @Column(nullable = false, unique = true)
  private String socialCode;

  public static SocialUserInfo newInstance(User user, SocialPlatform socialType,
      String socialCode) {
    return SocialUserInfo.builder()
        .user(user)
        .socialType(socialType)
        .socialCode(socialCode)
        .build();
  }

  public static String calculateSocialCode(SocialPlatform socialPlatform, String socialCode) {
    return socialPlatform.name() + "_" + socialCode;
  }
}
