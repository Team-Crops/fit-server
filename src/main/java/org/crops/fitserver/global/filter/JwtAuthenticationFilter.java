package org.crops.fitserver.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final PrincipalDetailsService principalDetailsService;
  private final HeaderTokenExtractor headerTokenExtractor;
  private final JwtResolver jwtResolver;

  private static final String[] whiteListPatterns = {
      "/actuator/**",
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/api-docs/**",
      "/webjars/**",
      "/docs/**",
      "/h2-console/**"
  };

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String accessToken = headerTokenExtractor.extractAccessToken(request);
    checkAccessTokenValidation(accessToken);
    setAuthenticationInSecurityContext(accessToken);
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    List<AntPathRequestMatcher> skipPathList = new ArrayList<>();
    Arrays.stream(whiteListPatterns)
        .map(AntPathRequestMatcher::new)
        .forEach(skipPathList::add);

    skipPathList.add(new AntPathRequestMatcher("/v1/auth/social/**"));

    OrRequestMatcher orRequestMatcher = new OrRequestMatcher(new ArrayList<>(skipPathList));
    return skipPathList.stream()
        .anyMatch(p -> orRequestMatcher.matches(request));
  }

  private void checkAccessTokenValidation(String accessToken) {
    if (!StringUtils.hasText(accessToken)) {
      throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
    }
    if (!jwtResolver.validateAccessToken(accessToken)) {
      log.warn("JWT Token is not validate : [{}]", accessToken);
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
      throw new BusinessException(e, ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
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
