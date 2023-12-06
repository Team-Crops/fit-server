package org.crops.fitserver.global.feign.oauth.google;

import org.crops.fitserver.global.feign.oauth.OAuthToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleAuthServerClient", url = "https://oauth2.googleapis.com")
public interface GoogleAuthServerClient {

  @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  OAuthToken getOAuth2AccessToken(
      @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
      @RequestParam("grant_type") String grantType,
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret,
      @RequestParam("redirect_uri") String redirectUri,
      @RequestParam("code") String code
  );
}
