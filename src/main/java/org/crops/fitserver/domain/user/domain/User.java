package org.crops.fitserver.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
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
  private String userName;

  @Column(length = 100)
  private String nickName;

  @Column(length = 20)
  private String phoneNumber;

  @Column(nullable = false)
  @ColumnDefault("false")
  private boolean isOpenPhoneNum;

  @Column(length = 2048)
  private String email;

  @Column(length = 100)
  private String career;

  @OneToOne(mappedBy = "user")
  private SocialUserInfo socialUserInfo;

  @OneToOne(mappedBy = "user")
  private UserInfo userInfo;

  public static User from(UserRole userRole) {
    return User.builder()
        .userRole(userRole)
        .build();
  }
}

