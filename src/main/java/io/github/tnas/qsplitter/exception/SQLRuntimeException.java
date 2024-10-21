package io.github.tnas.qsplitter.exception;

public class SQLRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SQLRuntimeException() {
		super();
	}

	public SQLRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SQLRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SQLRuntimeException(String message) {
		super(message);
	}

	public SQLRuntimeException(Throwable cause) {
		super(cause);
	}

}
