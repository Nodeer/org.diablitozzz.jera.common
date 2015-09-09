package org.diablitozzz.jera.db;

import java.sql.BatchUpdateException;

public class DbException extends Exception {

	private static final long serialVersionUID = 1L;

	private static Throwable resolveCause(Throwable cause) {
		if (cause instanceof BatchUpdateException) {
			return ((BatchUpdateException) cause).getNextException();
		}
		return cause;
	}

	public DbException(String message) {
		super(message);
	}

	public DbException(String message, Throwable cause) {
		super(message, DbException.resolveCause(cause));
	}

	public DbException(Throwable cause) {
		super(cause.getLocalizedMessage(), DbException.resolveCause(cause));
	}
}
