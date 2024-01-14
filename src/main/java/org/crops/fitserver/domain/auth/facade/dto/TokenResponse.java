package org.crops.fitserver.domain.auth.facade.dto;

import jakarta.validation.constraints.NotNull;
import org.crops.fitserver.global.jwt.TokenCollection;

public record TokenResponse(
    @NotNull String accessToken,
    @NotNull String refreshToken) {

  public static TokenResponse from(TokenCollection tokenCollection) {
    return new TokenResponse(
        tokenCollection.getAccessToken(),
        tokenCollection.getRefreshToken());
  }
}
