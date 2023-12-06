package org.crops.fitserver.global.exception;

import org.springframework.http.ResponseEntity;

public record ErrorResponse(ErrorCode errorCode) {

  public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(ErrorResponse.of(errorCode));
  }

  public static ErrorResponse of(ErrorCode errorCode) {
    return new ErrorResponse(errorCode);
  }
}
