package org.crops.fitserver.global.http;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.exception.ErrorType;
import org.crops.fitserver.global.exception.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class HeaderTokenExtractor {

	private static final String HEADER_PREFIX = "Bearer ";
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

	public String extractAccessToken(HttpServletRequest request) {
		String bearerHeader = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerHeader) && bearerHeader.startsWith(HEADER_PREFIX)) {
			return bearerHeader.substring(HEADER_PREFIX.length());
		}
		log.error("Authorization Header does not begin with \"Bearer\" String : [{}]", bearerHeader);
		throw new UnauthorizedException(ErrorType.INVALID_ACCESS_TOKEN_EXCEPTION);
	}

	public String extractRefreshToken(HttpServletRequest request){
		String bearerHeader = request.getHeader(REFRESH_TOKEN_HEADER);
		if (StringUtils.hasText(bearerHeader) && bearerHeader.startsWith(HEADER_PREFIX)) {
			return bearerHeader.substring(HEADER_PREFIX.length());
		}
		log.error("Refresh-Token Header does not begin with \"Bearer\" String : [{}]", bearerHeader);
		throw new UnauthorizedException(ErrorType.INVALID_REFRESH_TOKEN_EXCEPTION);
	}
}
