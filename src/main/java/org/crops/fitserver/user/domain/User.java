package org.crops.fitserver.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.global.entity.BaseEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {

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

	@Column(nullable = false)
	@ColumnDefault("false")
	private boolean isRest;

	@Column(nullable = false)
	@ColumnDefault("false")
	private boolean isValid;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private SocialUserInfo socialUserInfo;

	public static User newInstance() {
		return new User();
	}
}

