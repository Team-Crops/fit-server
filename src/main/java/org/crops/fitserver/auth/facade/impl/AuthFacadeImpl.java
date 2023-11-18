package org.crops.fitserver.auth.facade.impl;

import org.crops.fitserver.auth.controller.SocialPlatform;
import org.crops.fitserver.auth.facade.AuthFacade;
import org.springframework.stereotype.Component;

@Component
public class AuthFacadeImpl implements AuthFacade {

	@Override
	public void socialLogin(String redirectUrl, String authorizationCode, SocialPlatform socialPlatform) {
	}
}
