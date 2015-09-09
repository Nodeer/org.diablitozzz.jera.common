package org.diablitozzz.jera.pool;

public interface PoolResource {

	public void closePoolResource();

	public boolean isPooled();

	public void setPooled(boolean pooled);

}
