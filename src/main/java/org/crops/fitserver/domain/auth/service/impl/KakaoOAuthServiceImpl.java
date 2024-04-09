package org.crops.fitserver.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.feign.oauth.kakao.KakaoClientProperty;
import org.crops.fitserver.global.jwt.JwtProperty;
import org.crops.fitserver.domain.auth.service.OAuthService;
import org.crops.fitserver.global.feign.oauth.OAuthToken;
import org.crops.fitserver.global.feign.oauth.kakao.KakaoSocialUserProfile;
import org.crops.fitserver.global.feign.oauth.kakao.KakaoAuthServerClient;
import org.crops.fitserver.global.feign.oauth.kakao.KakaoServerClient;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.domain.user.domain.SocialUserInfo;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.crops.fitserver.domain.user.repository.SocialUserInfoRepository;
import org.crops.fitserver.domain.user.repository.UserRepository;
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
  public User socialUserLogin(String origin, String authorizationCode) {
    OAuthToken oAuthToken = kakaoAuthServerClient.getOAuth2AccessToken(
        kakaoClientProperty.getContentType(),
        kakaoClientProperty.getGrantType(),
        kakaoClientProperty.getClientId(),
        origin + kakaoClientProperty.getRedirectPath(),
        authorizationCode);

    KakaoSocialUserProfile socialUserProfile = kakaoServerClient.getUserInformation(
        jwtProperty.getBearerPrefix() + oAuthToken.getAccessToken());

    String socialCode = SocialUserInfo.calculateSocialCode(
        SocialPlatform.KAKAO,
        String.valueOf(socialUserProfile.getId()));

    return socialUserInfoRepository
        .findBySocialCode(socialCode)
        .map(SocialUserInfo::getUser)
        .orElseGet(() -> {
          User newUser = userRepository.save(
              User.from(UserRole.NON_MEMBER));

          return socialUserInfoRepository.save(
                  SocialUserInfo.newInstance(
                      newUser,
                      SocialPlatform.KAKAO,
                      socialCode))
              .getUser();
        });
  }

  @Override
  public String getLoginPageUrl(String origin) {
    return new StringBuilder()
        .append(kakaoClientProperty.getLoginPageUrl())
        .append("&client_id=")
        .append(kakaoClientProperty.getClientId())
        .append("&redirect_uri=")
        .append(origin)
        .append(kakaoClientProperty.getRedirectPath())
        .toString();
  }
}
