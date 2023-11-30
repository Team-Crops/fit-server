package org.crops.fitserver.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

	public static SocialUserInfo newInstance(User user, SocialPlatform socialType, String socialCode) {
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
