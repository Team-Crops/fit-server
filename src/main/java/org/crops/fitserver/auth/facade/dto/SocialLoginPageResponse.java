package org.crops.fitserver.auth.facade.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialLoginPageResponse {

	private String loginPageUrl;

	public static SocialLoginPageResponse from(String loginPageUrl) {
		return new SocialLoginPageResponse(loginPageUrl);
	}
}
