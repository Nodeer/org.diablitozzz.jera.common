package org.diablitozzz.jera.pool;

import java.util.concurrent.LinkedBlockingDeque;

public class PoolBasic<T extends PoolResource> implements Pool<T> {

	private volatile boolean closed = true;
	private final int maxSize;
	private final PoolResourceFactory<T> resourceFactory;
	private final LinkedBlockingDeque<T> queue = new LinkedBlockingDeque<>();
	private final PoolTime getTimeout;

	public PoolBasic(final int maxSize, final PoolResourceFactory<T> resourceFactory, final PoolTime getTimeout) {
		this.maxSize = maxSize;
		this.resourceFactory = resourceFactory;
		this.getTimeout = getTimeout;
	}

	@Override
	public void add(final T poolResource) {
		if (this.closed) {
			throw new IllegalStateException("Pool is not started");
		}
		if (poolResource.isPooled()) {
			throw new IllegalArgumentException("Resource already in pool");
		}
		poolResource.setPooled(true);
		this.queue.addLast(poolResource);
	}

	@Override
	public synchronized void close() {

		if (this.closed) {
			return;
		}

		//close and clear
		while (!this.queue.isEmpty()) {
			this.queue.poll().closePoolResource();
		}
		this.closed = true;
	}

	@Override
	public T get() throws Exception {
		if (this.closed) {
			this.start();
		}

		try {
			final T res = this.queue.pollLast(this.getTimeout.getDuration(), this.getTimeout.getTimeUnit());
			if (res == null) {
				throw new RuntimeException("Can't wait pool. Because timeout: " + this.getTimeout.getDuration() + " " + this.getTimeout.getTimeUnit());
			}
			res.setPooled(false);
			return res;
		} catch (final InterruptedException e) {
			throw new RuntimeException("Can't wait pool " + e.getMessage(), e);
		}
	}

	public int getMaxSize() {
		return this.maxSize;
	}

	@Override
	public boolean isClosed() {
		return this.closed;
	}

	@Override
	public synchronized void start() throws Exception {

		if (!this.closed) {
			return;
		}
		//init and create
		for (int i = 0; i < this.maxSize; i++) {
			final T resource = this.resourceFactory.createPoolResource(this);
			this.queue.add(resource);
			resource.setPooled(true);
		}
		this.closed = false;
	}

}
