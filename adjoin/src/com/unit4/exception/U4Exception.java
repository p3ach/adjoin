package com.unit4.exception;

public class U4Exception extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public U4Exception() {
	}

	public U4Exception(String message) {
		super(message);
	}

	public U4Exception(Throwable cause) {
		super(cause);
	}

	public U4Exception(String message, Throwable cause) {
		super(message, cause);
	}
}
