package org.diablitozzz.jera.db;


public interface DbConnectionCloseable extends DbConnection, AutoCloseable {

	@Override
	public void close();

}
