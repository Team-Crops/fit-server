package org.crops.fitserver.global.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class HttpResponse {

	public static ResponseEntity<Void> success(HttpStatus httpStatus){
		return ResponseEntity
				.status(httpStatus)
				.build();
	}

	public static <T> ResponseEntity<T> success(HttpStatus httpStatus, T data){
		return ResponseEntity
				.status(httpStatus)
				.body(data);
	}

	public static ResponseEntity<FailResponse> error(ErrorType errorType) {
		FailResponse response = new FailResponse(errorType.getStatusCode(), errorType.getMessage());
		return ResponseEntity
				.status(errorType.getStatusCode())
				.body(response);
	}

	public static ResponseEntity<FailResponse> error(ErrorType errorType, String message) {
		FailResponse response = new FailResponse(errorType.getStatusCode(), message);
		return ResponseEntity
				.status(errorType.getStatusCode())
				.body(response);
	}
}
