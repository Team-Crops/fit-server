package org.crops.fitserver.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.auth.dto.request.SocialLoginRequest;
import org.crops.fitserver.domain.auth.dto.response.SocialLoginPageResponse;
import org.crops.fitserver.domain.auth.dto.response.TokenResponse;
import org.crops.fitserver.domain.auth.facade.AuthFacade;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.dto.response.NoticeMessageResponse;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.global.annotation.V1;
import org.crops.fitserver.global.mq.MessagePublisher;
import org.crops.fitserver.global.mq.dto.Report;
import org.crops.fitserver.global.socket.service.SocketResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
      @RequestHeader("Origin") String origin,
      @PathVariable(name = "socialPlatform") SocialPlatform socialPlatform,
      @Valid @RequestBody SocialLoginRequest request
  ) {
    TokenResponse response = authFacade.socialLogin(
        origin,
        socialPlatform,
        request.code());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/social/{socialPlatform}/login-page")
  public ResponseEntity<SocialLoginPageResponse> getSocialLoginPageUrl(
      @RequestHeader("Origin") String origin,
      @PathVariable(name = "socialPlatform") SocialPlatform socialPlatform
  ) {
    SocialLoginPageResponse response =
        authFacade.getSocialLoginPageUrl(origin, socialPlatform);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/social/test/login")
  public ResponseEntity<TokenResponse> testLogin() {
    TokenResponse tokenResponse = authFacade.testLogin();
    return ResponseEntity.ok(tokenResponse);
  }

  @PostMapping("/social/test/login/{userId}")
  public ResponseEntity<TokenResponse> testLogin(@PathVariable(name = "userId") Long userId) {
    TokenResponse tokenResponse = authFacade.testLogin(userId);
    return ResponseEntity.ok(tokenResponse);
  }
}
