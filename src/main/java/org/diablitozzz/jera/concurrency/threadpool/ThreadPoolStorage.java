package org.diablitozzz.jera.concurrency.threadpool;

import java.util.ArrayList;
import java.util.List;

import org.diablitozzz.jera.data.list.ListException;
import org.diablitozzz.jera.data.list.ListNode;
import org.diablitozzz.jera.data.list.ListStorage;

public class ThreadPoolStorage {
    
    private final ListStorage busyThreads;
    private final ListStorage unbusyThreads;
    private final int capacity;
    private volatile int threadCount;
    
    public ThreadPoolStorage(final int capacity) {
        this.busyThreads = new ListStorage(capacity);
        this.unbusyThreads = new ListStorage(capacity);
        this.capacity = capacity;
        this.threadCount = 0;
    }
    
    synchronized public void clear() {
        this.unbusyThreads.clear();
        this.busyThreads.clear();
    }
    
    @Override
    protected void finalize() {
        this.clear();
    }
    
    private ThreadPoolWorkerThread findUnbusy(final ThreadPoolWorkerFactory workerFactory) {
        // есть свободный
        if (!this.unbusyThreads.isEmpty()) {
            return (ThreadPoolWorkerThread) this.unbusyThreads.getFirst().getValue();
        }
        // создаём новый
        if (this.threadCount < this.capacity) {
            final ThreadPoolWorkerThread thread = new ThreadPoolWorkerThread(workerFactory.createWorker(), this, new ListNode());
            //make thread name
            final String threadName = Thread.currentThread().getName() + " - worker - " + (this.threadCount + 1);
            thread.setName(threadName);
            this.threadCount++;
            return thread;
        }
        return null;
    }
    
    synchronized public ThreadPoolWorkerThread getUnbusy(final ThreadPoolWorkerFactory workerFactory) {
        ThreadPoolWorkerThread thread;
        
        while (true) {
            thread = this.findUnbusy(workerFactory);
            if (thread == null) {
                try {
                    this.wait();
                } catch (final InterruptedException e) {
                    return null;
                }
                continue;
            } else {
                return thread;
            }
        }
        
    }
    
    synchronized public void setBusy(final ThreadPoolWorkerThread thread) {
        try {
            this.unbusyThreads.delete(thread.getListNode());
            this.busyThreads.addLast(thread.getListNode());
        } catch (final ListException e) {
            e.printStackTrace();
        }
    }
    
    synchronized public void setUnbusy(final ThreadPoolWorkerThread thread) {
        try {
            this.busyThreads.delete(thread.getListNode());
            this.unbusyThreads.addFirst(thread.getListNode());
        } catch (final ListException e) {
            e.printStackTrace();
        }
        this.notify();
    }
    
    synchronized public List<ThreadPoolWorkerThread> toList() {
        final List<ThreadPoolWorkerThread> out = new ArrayList<ThreadPoolWorkerThread>(this.capacity);
        for (final ListNode item : this.unbusyThreads.toList()) {
            out.add((ThreadPoolWorkerThread) item.getValue());
        }
        for (final ListNode item : this.busyThreads.toList()) {
            out.add((ThreadPoolWorkerThread) item.getValue());
        }
        return out;
    }
    
}