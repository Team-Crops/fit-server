package org.crops.fitserver.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.auth.dto.request.SocialLoginRequest;
import org.crops.fitserver.domain.auth.dto.response.SocialLoginPageResponse;
import org.crops.fitserver.domain.auth.facade.AuthFacade;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.domain.auth.dto.response.TokenResponse;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthFacade authFacade;

  @PostMapping("/social/{socialPlatform}/login")
  public ResponseEntity<TokenResponse> socialLogin(
      @Valid @RequestBody SocialLoginRequest request,
      @PathVariable(name = "socialPlatform") SocialPlatform socialPlatform
  ) {
    TokenResponse tokenResponse = authFacade.socialLogin(
        request.code(),
        socialPlatform);
    return ResponseEntity.ok(tokenResponse);
  }

  @GetMapping("/social/{socialPlatform}/login-page")
  public ResponseEntity<SocialLoginPageResponse> getSocialLoginPageUrl(
      @PathVariable(name = "socialPlatform") SocialPlatform socialPlatform
  ) {
    SocialLoginPageResponse socialLoginPageResponse =
        authFacade.getSocialLoginPageUrl(socialPlatform);
    return ResponseEntity.ok(socialLoginPageResponse);
  }
}
