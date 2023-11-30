package org.crops.fitserver.auth.service.provider;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.auth.service.OAuthService;
import org.crops.fitserver.auth.service.impl.GoogleOAuthServiceImpl;
import org.crops.fitserver.auth.service.impl.KakaoOAuthServiceImpl;
import org.crops.fitserver.user.domain.SocialPlatform;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthServiceProvider {

	private static final Map<SocialPlatform, OAuthService> socialServiceMap = new HashMap<>();

	private final KakaoOAuthServiceImpl kakaoOAuthServiceImpl;
	private final GoogleOAuthServiceImpl googleOAuthServiceImpl;

	@PostConstruct
	private void initOAuthService() {
		socialServiceMap.put(SocialPlatform.KAKAO, kakaoOAuthServiceImpl);
		socialServiceMap.put(SocialPlatform.GOOGLE, googleOAuthServiceImpl);
	}

	public OAuthService getOAuthService(SocialPlatform socialPlatform) {
		return socialServiceMap.get(socialPlatform);
	}
}
