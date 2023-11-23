package org.crops.fitserver.auth.facade;

import org.crops.fitserver.auth.controller.SocialPlatform;
import org.crops.fitserver.auth.facade.dto.TokenResponse;

public interface AuthFacade {

	TokenResponse socialLogin(String redirectUrl, String authorizationCode, SocialPlatform socialPlatform);
}
