package org.crops.fitserver.global.exception;

public class NotFoundException extends FitException {

	public NotFoundException() {
		super(ErrorType.NOT_FOUND_RESOURCE_EXCEPTION);
	}
}
