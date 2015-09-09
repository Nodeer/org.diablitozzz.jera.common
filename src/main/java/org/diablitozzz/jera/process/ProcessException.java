package org.diablitozzz.jera.process;

public class ProcessException extends Exception {

	private static final long serialVersionUID = 1L;

	public ProcessException(final String message) {
		super(message);
	}

	public ProcessException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
