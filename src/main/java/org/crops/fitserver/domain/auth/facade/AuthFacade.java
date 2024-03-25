package org.crops.fitserver.domain.auth.facade;

import org.crops.fitserver.domain.auth.dto.response.SocialLoginPageResponse;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.domain.auth.dto.response.TokenResponse;

public interface AuthFacade {

  TokenResponse socialLogin(
      String authorizationCode,
      SocialPlatform socialPlatform);

  SocialLoginPageResponse getSocialLoginPageUrl(SocialPlatform socialPlatform);

  TokenResponse testLogin();

  TokenResponse testLogin(Long userId);
}
