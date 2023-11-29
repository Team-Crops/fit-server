package org.crops.fitserver.auth.facade.impl;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.auth.service.OAuthService;
import org.crops.fitserver.auth.service.provider.OAuthServiceProvider;
import org.crops.fitserver.user.domain.SocialPlatform;
import org.crops.fitserver.auth.facade.AuthFacade;
import org.crops.fitserver.auth.facade.dto.TokenResponse;
import org.crops.fitserver.global.jwt.TokenCollection;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {

	private final OAuthServiceProvider oAuthServiceProvider;

	@Override
	public TokenResponse socialLogin(String redirectUrl, String authorizationCode,
			SocialPlatform socialPlatform) {
		OAuthService oAuthService = oAuthServiceProvider.getOAuthService(socialPlatform);
		TokenCollection tokenCollection = oAuthService
				.socialLogin(
						redirectUrl,
						authorizationCode,
						socialPlatform);
		return TokenResponse.of(tokenCollection);
	}
}
