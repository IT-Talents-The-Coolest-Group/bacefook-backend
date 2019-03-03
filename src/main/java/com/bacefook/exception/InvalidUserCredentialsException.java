package com.bacefook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // code 422, used for wrong inputs
public class InvalidUserCredentialsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1901642121162240289L;

	public InvalidUserCredentialsException() {
		super();
	}

	public InvalidUserCredentialsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InvalidUserCredentialsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidUserCredentialsException(String arg0) {
		super(arg0);
	}

	public InvalidUserCredentialsException(Throwable arg0) {
		super(arg0);
	}

}
