package org.crops.fitserver.auth.facade.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crops.fitserver.global.jwt.TokenCollection;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenResponse {

  private final String accessToken;
  private final String refreshToken;

  public static TokenResponse of(TokenCollection tokenCollection) {
    return new TokenResponse(
        tokenCollection.getAccessToken(),
        tokenCollection.getRefreshToken());
  }
}
