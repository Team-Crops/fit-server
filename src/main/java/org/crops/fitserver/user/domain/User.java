package org.crops.fitserver.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, length = 10)
	@ColumnDefault("member")
	private UserRole userRole;

	@Column(length = 2048)
	private String profileImageUrl;

	@Column(length = 100)
	private String userName;

	@Column(nullable = false, length = 100)
	private String nickName;

	@Column(length = 20)
	private String phoneNumber;

	@Column(nullable = false)
	@ColumnDefault("false")
	private boolean isOpenPhoneNum;

	@Column(nullable = false, length = 2048)
	private String email;

	@Column(nullable = false, length = 100)
	private String career;

	@Column(nullable = false)
	@ColumnDefault("false")
	private boolean isRest;

	public static User of(Long id) {
		return User.builder()
			.id(id)
			.build();
	}
}

