package org.crops.fitserver.auth.facade;

import org.crops.fitserver.auth.controller.SocialPlatform;

public interface AuthFacade {

	void socialLogin(String redirectUrl, String authorizationCode, SocialPlatform socialPlatform);
}
