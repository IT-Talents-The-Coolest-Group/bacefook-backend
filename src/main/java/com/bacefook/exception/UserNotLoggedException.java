package com.bacefook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotLoggedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8785897022062479756L;

	public UserNotLoggedException() {
		super();
	}

	public UserNotLoggedException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public UserNotLoggedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UserNotLoggedException(String arg0) {
		super(arg0);
	}

	public UserNotLoggedException(Throwable arg0) {
		super(arg0);
	}
	

}
