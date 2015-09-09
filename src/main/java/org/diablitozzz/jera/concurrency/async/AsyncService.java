package org.diablitozzz.jera.concurrency.async;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncService implements AutoCloseable {
    
    private ExecutorService executorService;
    private String name;
    private int corePoolSize = 4;
    private int maximumPoolSize = 4;
    private long keepAliveTime = 1;
    private TimeUnit keepAliveTimeUnit = TimeUnit.MINUTES;
    private final AsyncTaskRunningSet taskRunningSet = new AsyncTaskRunningSet();
    
    @Override
    public void close() {
        if (this.executorService == null) {
            return;
        }
        while (!this.taskRunningSet.isEmpty()) {
            try {
                this.executorService.shutdownNow();
            } catch (final Throwable e) {
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (final InterruptedException e) {
            }
        }
        this.executorService = null;
    }
    
    private ExecutorService createExecutorService() {
        
        final AsyncServiceThreadFactory threadFactory = new AsyncServiceThreadFactory(this.name);
        final BlockingQueue<Runnable> workQueue = new SynchronousQueue<Runnable>();
        final RejectedExecutionHandler executionHandler = new ThreadPoolExecutor.AbortPolicy();
        final ExecutorService executorService = new ThreadPoolExecutor(
                this.corePoolSize,
                this.maximumPoolSize,
                this.keepAliveTime,
                this.keepAliveTimeUnit,
                workQueue,
                threadFactory,
                executionHandler);
        return executorService;
    }
    
    public void execute(final AsyncTask task) {
        
        if (this.taskRunningSet.isRunning(task)) {
            return;
        }
        
        if (this.executorService == null) {
            this.executorService = this.createExecutorService();
        }
        final AsyncTaskWrapper taskWrapper = new AsyncTaskWrapper(this.taskRunningSet, task);
        this.executorService.execute(taskWrapper);
    }
    
    public int getCorePoolSize() {
        return this.corePoolSize;
    }
    
    public long getKeepAliveTime() {
        return this.keepAliveTime;
    }
    
    public TimeUnit getKeepAliveTimeUnit() {
        return this.keepAliveTimeUnit;
    }
    
    public int getMaximumPoolSize() {
        return this.maximumPoolSize;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Collection<AsyncTask> getRunningTasks() {
        return this.taskRunningSet.getAll();
    }
    
    public boolean isClosed() {
        return this.executorService == null;
    }
    
    public void setCorePoolSize(final int corePoolSize) {
        this.corePoolSize = corePoolSize;
        if (this.corePoolSize > this.maximumPoolSize) {
            this.maximumPoolSize = this.corePoolSize;
        }
    }
    
    public void setKeepAliveTime(final long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
    
    public void setKeepAliveTimeUnit(final TimeUnit keepAliveTimeUnit) {
        this.keepAliveTimeUnit = keepAliveTimeUnit;
    }
    
    public void setMaximumPoolSize(final int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        if (this.maximumPoolSize <= this.corePoolSize) {
            this.corePoolSize = this.maximumPoolSize;
        }
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
}
