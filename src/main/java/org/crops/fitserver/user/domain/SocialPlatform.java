package org.crops.fitserver.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SocialPlatform {

	KAKAO("kakao"),
	GOOGLE("google"),
	;
	private final String name;

	public static SocialPlatform of(String name) {
		for (SocialPlatform socialPlatform : SocialPlatform.values()) {
			if (socialPlatform.name.equals(name)) {
				return socialPlatform;
			}
		}
		throw new IllegalArgumentException("존재하지 않는 소셜 플랫폼입니다.");
	}
}
