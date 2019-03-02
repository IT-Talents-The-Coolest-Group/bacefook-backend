package com.bacefook.exception;

public class GenderNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1608131520473809637L;

	public GenderNotFoundException() {
	}

	public GenderNotFoundException(String arg0) {
		super(arg0);
	}

	public GenderNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public GenderNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public GenderNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
