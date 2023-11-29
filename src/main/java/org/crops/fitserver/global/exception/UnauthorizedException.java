package org.crops.fitserver.global.exception;

public class UnauthorizedException extends FitException {

	public UnauthorizedException() {
		super(ErrorType.UNAUTHORIZED_EXCEPTION);
	}
}