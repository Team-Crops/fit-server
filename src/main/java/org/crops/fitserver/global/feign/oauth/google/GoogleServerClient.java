package org.crops.fitserver.global.feign.oauth.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleServerClient", url = "https://www.googleapis.com")
public interface GoogleServerClient {

	@PostMapping(value = "/oauth2/v3/userinfo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	GoogleSocialUserProfile getUserInformation(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken
	);
}
