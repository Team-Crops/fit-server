package org.crops.fitserver.global.exception;


import org.springframework.http.ResponseEntity;

public record ErrorResponse(String code, String message) {

  public static ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(ErrorResponse.from(errorCode));
  }

  public static ErrorResponse from(ErrorCode errorCode) {
    return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
  }

  public static ErrorResponse of(ErrorCode errorCode, String message) {
    return new ErrorResponse(errorCode.getCode(), errorCode.getMessage() + "\ndetail message: " + message);
  }
}
