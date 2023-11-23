package org.crops.fitserver.global.oauth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoApiClient", url = "https://kapi.kakao.com")
public interface KakaoV2 {

	@PostMapping(value = "/v2/user/me")
	SocialUserProfile getUserInformation(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken
	);
}
