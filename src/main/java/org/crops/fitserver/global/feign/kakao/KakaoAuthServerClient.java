package org.crops.fitserver.global.feign.kakao;

import org.crops.fitserver.global.feign.OAuthToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoAuthServerClient", url = "https://kauth.kakao.com")
public interface KakaoAuthServerClient {

	@PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	OAuthToken getOAuth2AccessToken(
			@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
			@RequestParam("grant_type") String grantType,
			@RequestParam("client_id") String clientId,
			@RequestParam("redirect_uri") String redirectUri,
			@RequestParam("code") String code
	);
}