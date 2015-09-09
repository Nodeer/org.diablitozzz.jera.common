package org.diablitozzz.jera.pool;

public interface PoolResourceFactory<T extends PoolResource> {

	public T createPoolResource(Pool<T> pool) throws Exception;

}
