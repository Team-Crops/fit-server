package org.crops.fitserver.domain.user.domain;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseTimeEntity {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false, length = 10)
  @ColumnDefault(value = "'MEMBER'")
  private UserRole userRole;

  @Column(length = 2048)
  private String profileImageUrl;

  @Column(length = 100)
  private String username;

  @Column(length = 100)
  private String nickname;

  @Column(length = 20)
  private String phoneNumber;

  @Column(nullable = false)
  @ColumnDefault("false")
  private boolean isOpenPhoneNum;

  @Column(length = 2048)
  private String email;

  @OneToOne(mappedBy = "user")
  private SocialUserInfo socialUserInfo;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserInfo userInfo;

  public static User from(UserRole userRole) {
    return User.builder()
        .userRole(userRole)
        .build();
  }

  @PrePersist
  public void prePersist() {
    this.userInfo = UserInfo.from(this);
  }

  public void updateUser(UpdateUserRequest updateUserRequest) {
    this.updateProfileImageUrl(updateUserRequest.getProfileImageUrl());
    this.updateUsername(updateUserRequest.getUsername());
    this.updateNickname(updateUserRequest.getNickname());
    this.updatePhoneNumber(updateUserRequest.getPhoneNumber());
    this.updateIsOpenPhoneNum(updateUserRequest.getIsOpenPhoneNum());
    this.updateEmail(updateUserRequest.getEmail());
    this.userInfo.updateUserInfo(updateUserRequest);
  }

  public void updateProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void updateUsername(String username) {
    this.username = username;
  }

  public void updateNickname(String nickname) {
    if (StringUtils.isNotBlank(this.nickname) && StringUtils.isBlank(nickname)) {
      throw new IllegalArgumentException("nickname cannot be null");
    }
    this.nickname = nickname;
  }

  public void updatePhoneNumber(String phoneNumber) {
    if (StringUtils.isNotBlank(this.phoneNumber) && StringUtils.isBlank(phoneNumber)) {
      throw new IllegalArgumentException("phoneNumber cannot be null");
    }
    this.phoneNumber = phoneNumber;
  }

  public void updateIsOpenPhoneNum(boolean isOpenPhoneNum) {
    this.isOpenPhoneNum = isOpenPhoneNum;
  }

  public void updateEmail(String email) {
    if (StringUtils.isNotBlank(this.email) && StringUtils.isBlank(email)) {
      throw new IllegalArgumentException("email cannot be null");
    }
    this.email = email;
  }
}

