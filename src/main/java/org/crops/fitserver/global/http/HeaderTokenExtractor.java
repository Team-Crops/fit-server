package org.crops.fitserver.global.http;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class HeaderTokenExtractor {

  private static final String BEARER_PREFIX = "Bearer ";
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

  public String extractAccessToken(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
    return extractAccessToken(authorizationHeader);
  }

  public String extractAccessToken(String authorizationHeader) {
    checkValidBearerToken(AUTHORIZATION_HEADER, authorizationHeader);
    return authorizationHeader.substring(BEARER_PREFIX.length());
  }

  public String extractRefreshToken(HttpServletRequest request) {
    String refreshTokenHeader = request.getHeader(REFRESH_TOKEN_HEADER);
    return extractRefreshToken(refreshTokenHeader);
  }

  public String extractRefreshToken(String refreshTokenHeader) {
    checkValidBearerToken(REFRESH_TOKEN_HEADER, refreshTokenHeader);
    return refreshTokenHeader.substring(BEARER_PREFIX.length());
  }

  private static void checkValidBearerToken(String headerName, String bearerToken) {
    if (!isValidBearerToken(bearerToken)) {
      log.error("{} Header does not begin with \"Bearer\" String : [{}]",
          headerName,
          bearerToken);
      throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
    }
  }

  private static boolean isValidBearerToken(String bearerToken) {
    return StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX);
  }
}
