package com.bacefook.exception;

public class UserExistsException extends Exception {

	public UserExistsException() {
		super();
	}

	public UserExistsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public UserExistsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UserExistsException(String arg0) {
		super(arg0);
	}

	public UserExistsException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3225086336946675459L;

}
