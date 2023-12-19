package org.crops.fitserver.domain.auth.service.provider;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.auth.service.OAuthService;
import org.crops.fitserver.global.exception.FitException;
import org.crops.fitserver.global.http.ErrorType;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
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
    throw new FitException(ErrorType.UNSUPPORTED_SOCIAL_PLATFORM_EXCEPTION);
  }
}
