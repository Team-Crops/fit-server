package org.crops.fitserver.auth.service;


import org.crops.fitserver.user.domain.SocialPlatform;
import org.crops.fitserver.user.domain.User;

public interface OAuthService {

	boolean support(SocialPlatform socialPlatform);

	User socialUserLogin(
			String redirectUrl,
			String authorizationCode);

	String getLoginPageUrl();
}
