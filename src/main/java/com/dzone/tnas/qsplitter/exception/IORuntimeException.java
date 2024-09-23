package com.dzone.tnas.qsplitter.exception;

public class IORuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IORuntimeException() {
		super();
	}

	public IORuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IORuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public IORuntimeException(String message) {
		super(message);
	}

	public IORuntimeException(Throwable cause) {
		super(cause);
	}

}
