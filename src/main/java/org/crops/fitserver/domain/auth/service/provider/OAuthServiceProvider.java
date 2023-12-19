package org.crops.fitserver.domain.auth.service.provider;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.auth.service.OAuthService;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthServiceProvider {

  private final List<OAuthService> socialServices;

  public OAuthService getOAuthService(SocialPlatform socialPlatform) {
    for (OAuthService oAuthService : socialServices) {
      if (oAuthService.support(socialPlatform)) {
        return oAuthService;
      }
    }
    throw new BusinessException(ErrorCode.UNSUPPORTED_SOCIAL_PLATFORM_EXCEPTION);
  }
}
