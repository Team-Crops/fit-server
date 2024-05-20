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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.recommend.domain.UserLikes;
import org.crops.fitserver.global.entity.BaseTimeEntity;
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
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE user_id = ?")
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

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserInfo userInfo;

  @OneToMany(mappedBy = "likeUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<UserLikes> likeUsers = new ArrayList<>();

  @OneToMany(mappedBy = "likedUser")
  private final List<UserLikes> likedUsers = new ArrayList<>();

  public static User from(UserRole userRole) {
    return User.builder()
        .userRole(userRole)
        .build();
  }

  @PrePersist
  public void prePersist() {
    this.userInfo = UserInfo.from(this);
  }

  public User withProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
    return this;
  }

  public User withUsername(String username) {
    this.username = username;
    return this;
  }

  public User withNickname(String nickname) {
    if (StringUtils.isNotBlank(this.nickname) && StringUtils.isBlank(nickname)) {
      throw new IllegalArgumentException("nickname cannot be null");
    }
    this.nickname = nickname;
    return this;
  }

  public User withPhoneNumber(String phoneNumber) {
    if (StringUtils.isNotBlank(this.phoneNumber) && StringUtils.isBlank(phoneNumber)) {
      throw new IllegalArgumentException("phoneNumber cannot be null");
    }
    this.phoneNumber = phoneNumber;
    return this;
  }

  public User withIsOpenPhoneNum(boolean isOpenPhoneNum) {
    this.isOpenPhoneNum = isOpenPhoneNum;
    return this;
  }

  public User withEmail(String email) {
    if (StringUtils.isNotBlank(this.email) && StringUtils.isBlank(email)) {
      throw new IllegalArgumentException("email cannot be null");
    }
    this.email = email;
    return this;
  }

  public void promoteRole(UserRole userRole) {
      this.userRole = userRole;
  }

  public void withdraw() {
    this.userRole = UserRole.WITHDRAW;
    this.profileImageUrl = null;
    this.username = null;
    this.nickname = null;
    this.phoneNumber = null;
    this.isOpenPhoneNum = false;
    this.email = null;
    this.userInfo.withdraw();
    this.likeUsers.clear();
  }
}

