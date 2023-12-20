package org.crops.fitserver.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public BusinessException(Throwable cause, ErrorCode errorCode) {
    super(errorCode.getMessage(), cause);
    this.errorCode = errorCode;
  }
}
