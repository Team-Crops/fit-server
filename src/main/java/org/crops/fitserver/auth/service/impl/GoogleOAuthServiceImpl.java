package org.crops.fitserver.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.feign.oauth.google.GoogleSocialUserProfile;
import org.crops.fitserver.global.feign.oauth.OAuthToken;
import org.crops.fitserver.global.feign.oauth.google.GoogleAuthServerClient;
import org.crops.fitserver.global.feign.oauth.google.GoogleClientProperty;
import org.crops.fitserver.global.feign.oauth.google.GoogleServerClient;
import org.crops.fitserver.auth.service.OAuthService;
import org.crops.fitserver.global.jwt.JwtProperty;
import org.crops.fitserver.user.domain.SocialPlatform;
import org.crops.fitserver.user.domain.SocialUserInfo;
import org.crops.fitserver.user.domain.User;
import org.crops.fitserver.user.domain.UserRole;
import org.crops.fitserver.user.repository.SocialUserInfoRepository;
import org.crops.fitserver.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GoogleOAuthServiceImpl implements OAuthService {

  private final JwtProperty jwtProperty;
  private final GoogleAuthServerClient googleAuthServerClient;
  private final GoogleServerClient googleServerClient;
  private final GoogleClientProperty googleClientProperty;
  private final SocialUserInfoRepository socialUserInfoRepository;
  private final UserRepository userRepository;

  @Override
  public boolean support(SocialPlatform socialPlatform) {
    return socialPlatform == SocialPlatform.GOOGLE;
  }

  @Override
  @Transactional
  public User socialUserLogin(
      String redirectUri,
      String authorizationCode) {

    OAuthToken oAuthToken = googleAuthServerClient.getOAuth2AccessToken(
        googleClientProperty.getContentType(),
        googleClientProperty.getGrantType(),
        googleClientProperty.getClientId(),
        googleClientProperty.getClientSecret(),
        redirectUri,
        authorizationCode);

    GoogleSocialUserProfile socialUserProfile = googleServerClient.getUserInformation(
        jwtProperty.getBearerPrefix() + oAuthToken.getAccessToken());

    String socialCode = SocialUserInfo.calculateSocialCode(
        SocialPlatform.GOOGLE,
        socialUserProfile.getSub());

    SocialUserInfo socialUserInfo = socialUserInfoRepository
        .findBySocialCode(socialCode)
        .orElseGet(
            () -> {
              User newUser = userRepository.save(
                  User.from(UserRole.NON_MEMBER));
              return socialUserInfoRepository.save(
                  SocialUserInfo.newInstance(
                      newUser,
                      SocialPlatform.GOOGLE,
                      socialCode));
            });

    return socialUserInfo.getUser();
  }

  @Override
  public String getLoginPageUrl() {
    return googleClientProperty.getLoginPageUrl();
  }
}
