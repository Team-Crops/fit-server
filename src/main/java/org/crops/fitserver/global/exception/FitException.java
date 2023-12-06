package org.crops.fitserver.global.exception;

import org.crops.fitserver.global.http.ErrorType;

public class FitException extends RuntimeException {

  private ErrorType errorType;

  public FitException(ErrorType errorType) {
    super(errorType.getMessage());
    this.errorType = errorType;
  }

  public ErrorType getErrorType() {
    return errorType;
  }
}
