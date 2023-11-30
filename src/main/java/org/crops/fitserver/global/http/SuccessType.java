package org.crops.fitserver.global.http;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessType {

	/**
	 * 200 OK
	 */
	LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
	REFRESH_TOKEN_SUCCESS(HttpStatus.OK, "토큰 갱신에 성공했습니다."),
	READ_RESOURCE_SUCCESS(HttpStatus.OK, "조회에 성공했습니다."),
	READ_RESOURCE_LIST_SUCCESS(HttpStatus.OK, "리스트 조회에 성공했습니다."),

	/**
	 * 201 CREATED
	 */
	CREATE_RESOURCE_SUCCESS(HttpStatus.CREATED, "생성에 성공했습니다."),
	SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료됐습니다."),

	/**
	 * 204 NO CONTENT
	 */
	UPDATE_RESOURCE_SUCCESS(HttpStatus.NO_CONTENT, "갱신에 성공했습니다."),
	DELETE_RESOURCE_SUCCESS(HttpStatus.NO_CONTENT, "삭제에 성공했습니다."),
	LOGOUT_SUCCESS(HttpStatus.NO_CONTENT, "로그아웃에 성공했습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}
}