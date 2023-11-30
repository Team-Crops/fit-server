package org.crops.fitserver.global.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.exception.FitException;
import org.crops.fitserver.global.http.HeaderTokenExtractor;
import org.crops.fitserver.global.http.ErrorType;
import org.crops.fitserver.global.exception.NotFoundException;
import org.crops.fitserver.global.jwt.JwtResolver;
import org.crops.fitserver.global.security.PrincipalDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
		if (StringUtils.hasText(accessToken)) {
			if (jwtResolver.validateAccessToken(accessToken)) {
				try {
					Long userId = jwtResolver.getUserIdFromAccessToken(accessToken);
					UserDetails userDetails =
							principalDetailsService.loadUserByUsername(userId.toString());
					Authentication authentication = getAuthenticationFromUserDetails(userDetails);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} catch (NotFoundException e) {
					throw new FitException(ErrorType.INVALID_ACCESS_TOKEN_EXCEPTION);
				}
			}
		} else {
			log.warn("JWT Token is null : [{}]", accessToken);
			throw new FitException(ErrorType.INVALID_ACCESS_TOKEN_EXCEPTION);
		}
		filterChain.doFilter(request, response);
	}

	private static UsernamePasswordAuthenticationToken getAuthenticationFromUserDetails(
			UserDetails userDetails) {
		return new UsernamePasswordAuthenticationToken(
				userDetails,
				"",
				userDetails.getAuthorities());
	}
}
