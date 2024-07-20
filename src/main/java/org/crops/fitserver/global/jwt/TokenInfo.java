package org.crops.fitserver.global.jwt;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crops.fitserver.domain.user.domain.User;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenInfo {

  private Map<String, Object> payload;

  public static TokenInfo from(User user) {
    Map<String, Object> accessTokenPayload = new HashMap<>();
    accessTokenPayload.put("userId", user.getId());
    return new TokenInfo(accessTokenPayload);
  }
}
