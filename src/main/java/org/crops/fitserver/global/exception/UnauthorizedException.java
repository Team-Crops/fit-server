package org.crops.fitserver.global.exception;

import org.crops.fitserver.global.http.ErrorType;

public class UnauthorizedException extends FitException {

	public UnauthorizedException() {
		super(ErrorType.UNAUTHORIZED_EXCEPTION);
	}
}