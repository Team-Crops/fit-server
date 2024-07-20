package org.crops.fitserver.domain.auth.service;


import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.domain.user.domain.User;

public interface OAuthService {

  boolean support(SocialPlatform socialPlatform);

  User socialUserLogin(String origin, String authorizationCode);

  String getLoginPageUrl(String origin);
}
