package org.crops.fitserver.global.exception;

import org.crops.fitserver.global.http.ErrorType;

public class NotFoundException extends FitException {

	public NotFoundException() {
		super(ErrorType.NOT_FOUND_RESOURCE_EXCEPTION);
	}
}
