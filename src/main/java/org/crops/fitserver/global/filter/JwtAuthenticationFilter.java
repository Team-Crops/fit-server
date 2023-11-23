package org.crops.fitserver.global.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.http.HeaderTokenExtractor;
import org.crops.fitserver.global.exception.ErrorType;
import org.crops.fitserver.global.exception.NotFoundException;
import org.crops.fitserver.global.exception.UnauthorizedException;
import org.crops.fitserver.global.jwt.JwtResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final HeaderTokenExtractor headerTokenExtractor;
	private final JwtResolver jwtResolver;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String jwtToken = headerTokenExtractor.extractAccessToken(request);
		if (StringUtils.hasText(jwtToken)) {
			if (jwtResolver.validateAccessToken(jwtToken)) {
				try {
					Authentication authentication = jwtResolver.getAuthenticationFromAccessToken(jwtToken);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} catch (NotFoundException e) {
					throw new UnauthorizedException(ErrorType.INVALID_ACCESS_TOKEN_EXCEPTION);
				}
			}
		} else {
			log.warn("JWT Token is null : [{}]", jwtToken);
			throw new UnauthorizedException(ErrorType.INVALID_ACCESS_TOKEN_EXCEPTION);
		}
		filterChain.doFilter(request, response);
	}
}
