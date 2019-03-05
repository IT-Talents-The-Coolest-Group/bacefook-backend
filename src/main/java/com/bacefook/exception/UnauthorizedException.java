package com.bacefook.exception;

public class UnauthorizedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8785897022062479756L;

	public UnauthorizedException() {
		super();
	}

	public UnauthorizedException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public UnauthorizedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnauthorizedException(String arg0) {
		super(arg0);
	}

	public UnauthorizedException(Throwable arg0) {
		super(arg0);
	}
	

}
