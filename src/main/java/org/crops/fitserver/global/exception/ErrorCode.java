package org.crops.fitserver.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  /**
   * common. code prefix: common-
   */
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "common-1", "서버 에러가 발생했습니다"),
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "common-2", "입력값이 올바르지 않습니다."),
  NOT_FOUND_RESOURCE_EXCEPTION(HttpStatus.NOT_FOUND, "common-3", "존재하지 않는 데이터입니다."),

  /**
   * auth. code prefix: auth-
   */
  UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "auth-1", "인증되지 않은 사용자입니다."),
  EXPIRED_ACCESS_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "auth-2", "만료된 엑세스 토큰입니다."),
  EXPIRED_REFRESH_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "auth-3", "만료된 리프레시 토큰입니다."),
  INVALID_ACCESS_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "auth-4", "유효하지 않은 엑세스 토큰입니다."),
  INVALID_REFRESH_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "auth-5", "유효하지 않은 리프레시 토큰입니다."),
  UNSUPPORTED_JWT_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "auth-6", "지원하지 않는 JWT 토큰입니다."),
  UNSUPPORTED_SOCIAL_PLATFORM_EXCEPTION(HttpStatus.UNAUTHORIZED, "auth-7", "지원하지 않는 소셜 플랫폼입니다."),

  /**
   * resource. code prefix: resource-
   */
  FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "resource-1", "리소스에 접근 권한이 없습니다."),

  /**
   * file. code prefix: file-
   */
  FILE_UPLOAD_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "file-1", "파일 업로드에 실패했습니다.");


  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
