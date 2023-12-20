package org.crops.fitserver.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.global.http.HeaderTokenExtractor;
import org.crops.fitserver.global.jwt.JwtResolver;
import org.crops.fitserver.global.security.PrincipalDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final PrincipalDetailsService principalDetailsService;
  private final HeaderTokenExtractor headerTokenExtractor;
  private final JwtResolver jwtResolver;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String accessToken = headerTokenExtractor.extractAccessToken(request);
    checkAccessTokenNotNull(accessToken);
    checkAccessTokenValidation(accessToken);
    setAuthenticationInSecurityContext(accessToken);
    filterChain.doFilter(request, response);
  }

  private static void checkAccessTokenNotNull(String accessToken) {
    if (!StringUtils.hasText(accessToken)) {
      log.warn("JWT Token is null : [{}]", accessToken);
      throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
    }
  }

  private void checkAccessTokenValidation(String accessToken) {
    if (!jwtResolver.validateAccessToken(accessToken)) {
      throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
    }
  }

  private void setAuthenticationInSecurityContext(String accessToken) {
    try {
      Long userId = jwtResolver.getUserIdFromAccessToken(accessToken);
      UserDetails userDetails =
          principalDetailsService.loadUserByUsername(userId.toString());
      Authentication authentication = getAuthenticationFromUserDetails(userDetails);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (UsernameNotFoundException e) {
      throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
    }
  }

  private static UsernamePasswordAuthenticationToken getAuthenticationFromUserDetails(
      UserDetails userDetails) {
    return new UsernamePasswordAuthenticationToken(
        userDetails,
        "",
        userDetails.getAuthorities());
  }
}
