package org.crops.fitserver.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    log.error("BusinessException : {}", e.getErrorCode().getMessage(), e);
    return ErrorResponse.toResponseEntity(e.getErrorCode());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException : {}", e.getMessage(), e);
    return ErrorResponse.toResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Exception : {}", e.getMessage(), e);
    return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
  }
}
