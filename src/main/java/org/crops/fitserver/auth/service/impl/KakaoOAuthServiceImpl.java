package org.crops.fitserver.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.feign.oauth.kakao.KakaoClientProperty;
import org.crops.fitserver.global.jwt.JwtProperty;
import org.crops.fitserver.auth.service.OAuthService;
import org.crops.fitserver.global.feign.oauth.OAuthToken;
import org.crops.fitserver.global.feign.oauth.kakao.KakaoSocialUserProfile;
import org.crops.fitserver.global.feign.oauth.kakao.KakaoAuthServerClient;
import org.crops.fitserver.global.feign.oauth.kakao.KakaoServerClient;
import org.crops.fitserver.user.domain.SocialPlatform;
import org.crops.fitserver.user.domain.SocialUserInfo;
import org.crops.fitserver.user.domain.User;
import org.crops.fitserver.user.repository.SocialUserInfoRepository;
import org.crops.fitserver.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KakaoOAuthServiceImpl implements OAuthService {

	private final JwtProperty jwtProperty;
	private final KakaoAuthServerClient kakaoAuthServerClient;
	private final KakaoServerClient kakaoServerClient;
	private final KakaoClientProperty kakaoClientProperty;
	private final SocialUserInfoRepository socialUserInfoRepository;
	private final UserRepository userRepository;

	@Override
	public boolean support(SocialPlatform socialPlatform) {
		return socialPlatform == SocialPlatform.KAKAO;
	}

	@Override
	@Transactional
	public User socialUserLogin(
			String redirectUrl,
			String authorizationCode) {

		OAuthToken oAuthToken = kakaoAuthServerClient.getOAuth2AccessToken(
				kakaoClientProperty.getContentType(),
				kakaoClientProperty.getGrantType(),
				kakaoClientProperty.getClientId(),
				redirectUrl,
				authorizationCode);

		KakaoSocialUserProfile socialUserProfile = kakaoServerClient.getUserInformation(
				jwtProperty.getBearerPrefix() + oAuthToken.getAccessToken());

		String socialCode = SocialUserInfo.calculateSocialCode(
				SocialPlatform.KAKAO,
				String.valueOf(socialUserProfile.getId()));

		SocialUserInfo socialUserInfo = socialUserInfoRepository
				.findBySocialCode(socialCode)
				.orElseGet(
						() -> {
							User newUser = userRepository.save(
									User.newInstance());
							return socialUserInfoRepository.save(
									SocialUserInfo.newInstance(
											newUser,
											SocialPlatform.KAKAO,
											socialCode));
						});

		return socialUserInfo.getUser();
	}

	@Override
	public String getLoginPageUrl() {
		return kakaoClientProperty.getLoginPageUrl();
	}
}
