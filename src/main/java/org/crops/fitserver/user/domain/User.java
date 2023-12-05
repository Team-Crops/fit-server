package org.crops.fitserver.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

	public static User from(UserRole userRole) {
		return User.builder()
			.userRole(userRole)
			.build();
	}
}

