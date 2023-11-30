package org.crops.fitserver.global.http;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {

	/**
	 * 401 UNAUTHORIZED
	 */
	UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
	EXPIRED_ACCESS_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 엑세스 토큰입니다."),
	EXPIRED_REFRESH_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),
	INVALID_ACCESS_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 엑세스 토큰입니다."),
	INVALID_REFRESH_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
	UNSUPPORTED_JWT_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),

	/**
	 * 403 Forbidden
	 */
	FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "리소스에 접근 권한이 없습니다."),


	/**
	 * 404 NOT FOUND
	 */
	NOT_FOUND_RESOURCE_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 데이터입니다."),

	/**
	 * 500 INTERNAL SERVER ERROR
	 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다"),

	;
	private final HttpStatus httpStatus;
	private final String message;

	public int getStatusCode() {
		return httpStatus.value();
	}

	public String getMessage() {
		return message;
	}
}
