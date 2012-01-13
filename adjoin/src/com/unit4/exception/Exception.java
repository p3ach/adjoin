package com.unit4.exception;

public class Exception extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Exception() {
	}

	public Exception(String message) {
		super(message);
	}

	public Exception(Throwable cause) {
		super(cause);
	}

	public Exception(String message, Throwable cause) {
		super(message, cause);
	}
}
