package org.crops.fitserver.domain.auth.facade;

import org.crops.fitserver.domain.auth.facade.dto.SocialLoginPageResponse;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.domain.auth.facade.dto.TokenResponse;

public interface AuthFacade {

  TokenResponse socialLogin(
      String redirectUrl,
      String authorizationCode,
      SocialPlatform socialPlatform);

  SocialLoginPageResponse getSocialLoginPageUrl(SocialPlatform socialPlatform);
}
