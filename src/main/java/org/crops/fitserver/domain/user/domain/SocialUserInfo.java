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

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialUserInfo {

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

  @Column(nullable = false, length = 255)
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
