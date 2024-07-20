package org.crops.fitserver.domain.auth.dto.response;

import jakarta.validation.constraints.NotNull;

public record SocialLoginPageResponse(
    @NotNull String loginPageUrl) {

  public static SocialLoginPageResponse from(String loginPageUrl) {
    return new SocialLoginPageResponse(loginPageUrl);
  }
}
