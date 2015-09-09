package org.diablitozzz.jera.db.log;

public interface DbLogHandler {

	void log(final long start, final String message, final Object[] params, final Throwable error);

}
