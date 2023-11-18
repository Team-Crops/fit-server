package org.crops.fitserver.global.openFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao", url = "https://kauth.kakao.com")
public interface Kakao {

	@PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	void getOAuth2AccessToken(
			@RequestParam("grant_type") String grantType,
			@RequestParam("client_id") String clientId,
			@RequestParam("redirect_uri") String redirectUri,
			@RequestParam("code") String code
	);
}
