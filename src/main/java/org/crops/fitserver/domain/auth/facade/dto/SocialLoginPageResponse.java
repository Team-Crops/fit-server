package org.crops.fitserver.domain.auth.facade.dto;

import jakarta.validation.constraints.NotNull;

public record SocialLoginPageResponse(
    @NotNull String loginPageUrl) {

  public static SocialLoginPageResponse from(String loginPageUrl) {
    return new SocialLoginPageResponse(loginPageUrl);
  }
}
