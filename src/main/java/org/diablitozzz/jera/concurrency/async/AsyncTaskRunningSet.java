package org.diablitozzz.jera.concurrency.async;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncTaskRunningSet {
    
    private final Map<AsyncTask, Object> map = new ConcurrentHashMap<>();
    
    public void add(final AsyncTask task) {
        this.map.put(task, new Object());
    }
    
    public Collection<AsyncTask> getAll() {
        return Collections.unmodifiableCollection(this.map.keySet());
    }
    
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    public boolean isRunning(final AsyncTask task) {
        return this.map.containsKey(task);
    }
    
    public void remove(final AsyncTask task) {
        this.map.remove(task);
    }
    
}
