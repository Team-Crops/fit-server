package org.crops.fitserver.global.http;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.exception.FitException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class HeaderTokenExtractor {

	private static final String HEADER_PREFIX = "Bearer ";
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

	public String extractAccessToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		checkValidBearerToken(AUTHORIZATION_HEADER, bearerToken);
		return bearerToken.substring(HEADER_PREFIX.length());
	}

	public String extractRefreshToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(REFRESH_TOKEN_HEADER);
		checkValidBearerToken(REFRESH_TOKEN_HEADER, bearerToken);
		return bearerToken.substring(HEADER_PREFIX.length());
	}

	private static void checkValidBearerToken(String headerPrefix, String bearerToken) {
		if (!isValidBearerToken(bearerToken)) {
			log.error("{} Header does not begin with \"Bearer\" String : [{}]",
					headerPrefix,
					bearerToken);
			throw new FitException(ErrorType.INVALID_ACCESS_TOKEN_EXCEPTION);
		}
	}

	private static boolean isValidBearerToken(String bearerHeader) {
		return StringUtils.hasText(bearerHeader) && bearerHeader.startsWith(HEADER_PREFIX);
	}
}
