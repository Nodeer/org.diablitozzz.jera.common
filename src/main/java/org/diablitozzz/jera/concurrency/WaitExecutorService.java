package org.diablitozzz.jera.concurrency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class WaitExecutorService {
    
    private final ExecutorService executorService;
    private final List<Future<?>> futures = new ArrayList<>();

    public WaitExecutorService(final ExecutorService executorService) {
        this.executorService = executorService;
    }

    public WaitExecutorService execute(final Runnable e) {
        
        final Future<?> future = this.executorService.submit(() -> {
            e.run();
            return null;

        });
        this.futures.add(future);
        return this;
    }
    
    public WaitExecutorService shutdown() {
        this.executorService.shutdownNow();
        return this;
    }
    
    public WaitExecutorService waitForEnd() throws InterruptedException, ExecutionException {
        final Iterator<Future<?>> iterator = this.futures.iterator();
        while (iterator.hasNext()) {
            iterator.next().get();
            iterator.remove();
        }
        return this;
    }
}
