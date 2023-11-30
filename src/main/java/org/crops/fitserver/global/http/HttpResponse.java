package org.crops.fitserver.global.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class HttpResponse {

	public static ResponseEntity<Void> success(SuccessType successType){
		log.info(successType.getMessage());
		return ResponseEntity
				.status(successType.getHttpStatus())
				.build();
	}

	public static <T> ResponseEntity<T> success(SuccessType successType, T data){
		log.info(successType.getMessage());
		return ResponseEntity
				.status(successType.getHttpStatus())
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
