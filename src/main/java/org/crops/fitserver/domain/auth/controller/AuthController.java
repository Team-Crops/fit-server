package org.crops.fitserver.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.auth.facade.dto.SocialLoginPageResponse;
import org.crops.fitserver.domain.auth.facade.AuthFacade;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.domain.auth.facade.dto.TokenResponse;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthFacade authFacade;

  @GetMapping("/social/{socialPlatform}/login")
  public ResponseEntity<TokenResponse> socialLogin(
      HttpServletRequest request,
      @RequestParam(name = "code") String code,
      @PathVariable(name = "socialPlatform") SocialPlatform socialPlatform
  ) {
    String requestUrl = request.getRequestURL().toString();
    if (!StringUtils.hasText(requestUrl)) {
      throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
    }
    TokenResponse tokenResponse = authFacade.socialLogin(
        requestUrl,
        code,
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
