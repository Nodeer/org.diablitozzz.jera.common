package org.diablitozzz.jera.pool;

public interface Pool<T extends PoolResource> extends AutoCloseable {

	public void add(T poolResource);

	@Override
	public void close();

	public T get() throws Exception;

	public boolean isClosed();

	public void start() throws Exception;
}
