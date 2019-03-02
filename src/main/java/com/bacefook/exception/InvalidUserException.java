package com.bacefook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)//TODO status code
public class InvalidUserException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1901642121162240289L;

	public InvalidUserException() {
		super();
	}

	public InvalidUserException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InvalidUserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidUserException(String arg0) {
		super(arg0);
	}

	public InvalidUserException(Throwable arg0) {
		super(arg0);
	}

}
