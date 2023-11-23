package org.crops.fitserver.global.exception;

public class UnauthorizedException extends FitException {

	public UnauthorizedException() {
		super(ErrorType.UNAUTHORIZED_EXCEPTION);
	}

	public UnauthorizedException(ErrorType errorType) {
		super(ErrorType.UNAUTHORIZED_EXCEPTION, errorType.getMessage());
	}
}