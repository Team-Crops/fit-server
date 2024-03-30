package org.crops.fitserver.domain.auth.facade;

import org.crops.fitserver.domain.auth.dto.response.SocialLoginPageResponse;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.domain.auth.dto.response.TokenResponse;

public interface AuthFacade {

  TokenResponse socialLogin(
      String origin,
      SocialPlatform socialPlatform,
      String authorizationCode);

  SocialLoginPageResponse getSocialLoginPageUrl(String origin, SocialPlatform socialPlatform);
}
