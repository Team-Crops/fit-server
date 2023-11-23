package org.crops.fitserver.auth.facade.impl;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.auth.controller.SocialPlatform;
import org.crops.fitserver.auth.facade.AuthFacade;
import org.crops.fitserver.auth.facade.dto.TokenResponse;
import org.crops.fitserver.global.jwt.JwtProvider;
import org.crops.fitserver.global.jwt.TokenCollection;
import org.crops.fitserver.global.jwt.TokenInfo;
import org.crops.fitserver.global.oauth.Kakao;
import org.crops.fitserver.global.oauth.KakaoV2;
import org.crops.fitserver.global.oauth.OAuthToken;
import org.crops.fitserver.global.oauth.SocialUserProfile;
import org.crops.fitserver.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {

	private final Kakao kakao;
	private final KakaoV2 kakaoV2;
	private final JwtProvider jwtProvider;


	@Value("${oauth.kakao.client_id}")
	private String clientId;

	@Override
	public TokenResponse socialLogin(String redirectUrl, String authorizationCode, SocialPlatform socialPlatform) {
		OAuthToken authorizationCode1 = kakao.getOAuth2AccessToken(
				"application/x-www-form-urlencoded;charset=utf-8",
				"authorization_code",
				clientId,
				redirectUrl,
				authorizationCode);
		SocialUserProfile userInformation = kakaoV2.getUserInformation(
				"Bearer " + authorizationCode1.getAccessToken());
		User user = User.of(userInformation.getId());
		TokenCollection tokenCollection =
				jwtProvider.createTokenCollection(TokenInfo.from(user));
		return TokenResponse.of(tokenCollection);
	}
}
