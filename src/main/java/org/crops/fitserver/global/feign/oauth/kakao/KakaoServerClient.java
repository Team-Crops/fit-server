package org.crops.fitserver.global.feign.oauth.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoApiClient", url = "https://kapi.kakao.com")
public interface KakaoServerClient {

	@PostMapping(value = "/v2/user/me")
	KakaoSocialUserProfile getUserInformation(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken
	);
}
