package org.diablitozzz.jera.migration;

public class MigrationException extends Exception {

	private static final long serialVersionUID = 1L;

	public MigrationException() {
		super();
	}

	public MigrationException(final String message) {
		super(message);
	}

	public MigrationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MigrationException(final Throwable cause) {
		super(cause);
	}

}
