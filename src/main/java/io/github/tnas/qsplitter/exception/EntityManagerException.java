package io.github.tnas.qsplitter.exception;

public class EntityManagerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityManagerException() {
		super();
	}

	public EntityManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EntityManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityManagerException(String message) {
		super(message);
	}

	public EntityManagerException(Throwable cause) {
		super(cause);
	}

}
