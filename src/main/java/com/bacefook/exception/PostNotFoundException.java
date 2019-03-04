package com.bacefook.exception;

public class PostNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8826441191134416075L;

	public PostNotFoundException() {
		super();
	}

	public PostNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public PostNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PostNotFoundException(String arg0) {
		super(arg0);
	}

	public PostNotFoundException(Throwable arg0) {
		super(arg0);
	}

}
