package com.bacefook.exception;

public class AlreadyContainsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 946038991076699295L;

	public AlreadyContainsException() {
		super();
	}

	public AlreadyContainsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public AlreadyContainsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AlreadyContainsException(String arg0) {
		super(arg0);
	}

	public AlreadyContainsException(Throwable arg0) {
		super(arg0);
	}
	

}
