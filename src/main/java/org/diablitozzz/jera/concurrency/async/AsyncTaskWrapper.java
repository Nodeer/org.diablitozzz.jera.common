package org.diablitozzz.jera.concurrency.async;

public class AsyncTaskWrapper implements Runnable {
    
    private final AsyncTaskRunningSet runningSet;
    private final AsyncTask task;
    
    public AsyncTaskWrapper(final AsyncTaskRunningSet runningSet, final AsyncTask task) {
        this.runningSet = runningSet;
        this.task = task;
    }
    
    @Override
    public void run() {
        try {
            this.runningSet.add(this.task);
            this.task.run();
        } finally {
            this.runningSet.remove(this.task);
        }
        
    }
    
}
