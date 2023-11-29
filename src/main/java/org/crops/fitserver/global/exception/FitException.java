package org.crops.fitserver.global.exception;

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
