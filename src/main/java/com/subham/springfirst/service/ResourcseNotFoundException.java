package com.subham.springfirst.service;

public class ResourcseNotFoundException extends Exception {
	private static final long serialVersionUID = -9151683449874463423L;

	public ResourcseNotFoundException() {
		super();
	}

	public ResourcseNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ResourcseNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourcseNotFoundException(String message) {
		super(message);
	}

	public ResourcseNotFoundException(Throwable cause) {
		super(cause);
	}

}
