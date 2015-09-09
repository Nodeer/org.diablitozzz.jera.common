package org.diablitozzz.jera.db.log;

import java.util.ArrayList;
import java.util.Collection;

public class DbLogService implements AutoCloseable {

	private final Collection<DbLogHandler> handlers = new ArrayList<>();

	@Override
	public void close() throws Exception {
		this.handlers.clear();
	}

	public Collection<DbLogHandler> getHandlers() {
		return this.handlers;
	}

	public void log(final long start, final String message, final Object[] params, final Throwable error) {
		for (final DbLogHandler handler : this.handlers) {
			handler.log(start, message, params, error);
		}
	}

}
