package org.crops.fitserver.auth.service;


import org.crops.fitserver.user.domain.User;

public interface OAuthService {

	User socialUserLogin(
			String redirectUrl,
			String authorizationCode);

	String getLoginPageUrl();
}
