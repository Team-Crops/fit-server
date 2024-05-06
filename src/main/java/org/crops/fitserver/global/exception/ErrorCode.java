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
  DUPLICATED_RESOURCE_EXCEPTION(HttpStatus.CONFLICT, "common-4", "이미 존재하는 데이터입니다."),
  PARSE_JSON_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "common-5", "JSON 파싱 에러가 발생했습니다."),
  UNSUPPORTED_MESSAGE_TYPE_EXCEPTION(HttpStatus.BAD_REQUEST, "common-7", "지원하지 않는 채팅 메세지 타입입니다."),
  NOT_FOUND_HANDLER_EXCEPTION(HttpStatus.NOT_FOUND, "common-8", "지원하지 않는 Api 요청 입니다."),
  NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "common-9", "존재하지 않는 사용자입니다."),
  INVALID_ACCESS_EXCEPTION(HttpStatus.FORBIDDEN, "common-10", "잘못된 접근입니다."),
  BLOCKED_USER_EXCEPTION(HttpStatus.FORBIDDEN, "common-11", "활동제한이 걸린 사용자입니다."),

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
  FILE_UPLOAD_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "file-1", "파일 업로드에 실패했습니다."),

  /**
   * user-policy-agreement. code prefix: user-policy-agreement-
   */
  DUPLICATED_POLICY_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "user-policy-agreement-1",
      "같은 약관을 한 번에 여러 번 동의할 수 없습니다."),

  /**
   * matching. code prefix: matching-
   */
  ALREADY_EXIST_MATCHING_EXCEPTION(HttpStatus.CONFLICT, "matching-1", "이미 매칭이 존재합니다."),
  NOT_EXIST_MATCHING_EXCEPTION(HttpStatus.NOT_FOUND, "matching-2", "매칭이 존재하지 않습니다."),
  NOT_EXIST_MATCHING_ROOM_EXCEPTION(HttpStatus.NOT_FOUND, "matching-3", "매칭 방이 존재하지 않습니다."),
  NOT_ENOUGH_MATCHING_EXCEPTION(HttpStatus.NOT_ACCEPTABLE, "matching-4", "매칭 인원 수가 충분하지 않습니다."),
  NOT_READY_MATCHING_EXCEPTION(HttpStatus.NOT_ACCEPTABLE, "matching-5", "모든 인원이 매칭 준비가 되지 않았습니다."),
  NOT_ENABLE_READY_EXCEPTION(HttpStatus.FORBIDDEN, "matching-6", "임시 방장은 준비할 수 없습니다."),
  ALREADY_EXIST_MATCHING_ROOM_EXCEPTION(HttpStatus.CONFLICT, "matching-7", "이미 매칭방이 존재합니다."),

  /**
   * project. code prefix: project-
   */
  NOT_EXIST_PROJECT_EXCEPTION(HttpStatus.NOT_FOUND, "project-1", "프로젝트가 존재하지 않습니다."),
  ;


  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
