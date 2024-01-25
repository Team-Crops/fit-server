package org.crops.fitserver.domain.auth.facade.impl;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.auth.facade.dto.SocialLoginPageResponse;
import org.crops.fitserver.domain.auth.service.OAuthService;
import org.crops.fitserver.domain.auth.service.provider.OAuthServiceProvider;
import org.crops.fitserver.domain.auth.facade.AuthFacade;
import org.crops.fitserver.global.jwt.JwtProvider;
import org.crops.fitserver.global.jwt.TokenInfo;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.domain.auth.facade.dto.TokenResponse;
import org.crops.fitserver.domain.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {

  private final OAuthServiceProvider oAuthServiceProvider;
  private final JwtProvider jwtProvider;

  @Override
  @Transactional
  public TokenResponse socialLogin(
      String redirectUrl,
      String authorizationCode,
      SocialPlatform socialPlatform) {
    OAuthService oAuthService = oAuthServiceProvider.getService(socialPlatform);
    User user = oAuthService.socialUserLogin(
        redirectUrl,
        authorizationCode);
    return TokenResponse.from(
        jwtProvider.createTokenCollection(
            TokenInfo.from(user)));
  }

  @Override
  @Transactional(readOnly = true)
  public SocialLoginPageResponse getSocialLoginPageUrl(SocialPlatform socialPlatform) {
    OAuthService oAuthService = oAuthServiceProvider.getService(socialPlatform);
    return SocialLoginPageResponse.from(oAuthService.getLoginPageUrl());
  }
}