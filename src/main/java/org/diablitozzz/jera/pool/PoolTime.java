package org.diablitozzz.jera.pool;

import java.util.concurrent.TimeUnit;

public class PoolTime {

	private final TimeUnit timeUnit;
	private final long duration;

	public PoolTime(long duration, TimeUnit timeUnit) {
		this.duration = duration;
		this.timeUnit = timeUnit;
	}

	public long getDuration() {
		return this.duration;
	}

	public TimeUnit getTimeUnit() {
		return this.timeUnit;
	}

}
