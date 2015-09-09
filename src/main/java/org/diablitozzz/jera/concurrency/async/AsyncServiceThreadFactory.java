package org.diablitozzz.jera.concurrency.async;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncServiceThreadFactory implements ThreadFactory {
    
    private final String name;
    private final AtomicInteger threadNumber;
    private final ThreadGroup threadGroup;
    
    public AsyncServiceThreadFactory(final String name) {
        this.name = name;
        this.threadNumber = new AtomicInteger(1);
        this.threadGroup = new ThreadGroup(name);
    }
    
    @Override
    public Thread newThread(final Runnable runnable) {
        
        final String threadName = this.name + "-" + this.threadNumber.getAndIncrement();
        final Thread thread = new Thread(this.threadGroup, runnable, threadName, 0);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
    
}
