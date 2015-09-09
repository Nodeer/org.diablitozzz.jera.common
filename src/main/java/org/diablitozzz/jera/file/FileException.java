package org.diablitozzz.jera.file;

public class FileException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileException(String message) {
		super(message);
	}

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileException(Throwable cause) {
		super(cause.getLocalizedMessage(), cause);
	}

}
