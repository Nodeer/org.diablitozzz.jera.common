package org.diablitozzz.jera.concurrency.threadpool;

import org.diablitozzz.jera.data.list.ListNode;

public class ThreadPoolWorkerThread extends Thread {
    
    private final ThreadPoolWorker worker;
    private Object message;
    private volatile boolean canRun = false;
    private final ThreadPoolStorage storage;
    private final ListNode listNode;
    private boolean hasNewMessage = false;
    
    public ThreadPoolWorkerThread(final ThreadPoolWorker worker, final ThreadPoolStorage storage, final ListNode listNode) {
        this.listNode = listNode;
        this.listNode.setValue(this);
        this.worker = worker;
        this.storage = storage;
    }
    
    synchronized public void execute(final Object message) {
        this.message = message;
        this.storage.setBusy(this);
        this.hasNewMessage = true;
        this.notify();
    }
    
    @Override
    protected void finalize() {
        this.canRun = false;
        this.worker.destroy();
    }
    
    public ListNode getListNode() {
        return this.listNode;
    }
    
    public boolean isStarted() {
        return this.canRun != false;
    }
    
    @Override
    public void run() {
        this.worker.init();
        
        while (this.canRun != false) {
            
            // ждём пока сообщение пусто
            synchronized (this) {
                if (!this.hasNewMessage) {
                    try {
                        this.wait();
                        continue;
                    } catch (final InterruptedException e) {
                        break;
                    }
                }
            }
            this.worker.service(this.message);
            // говорим что не занят
            synchronized (this) {
                this.message = null;
                this.hasNewMessage = false;
                this.storage.setUnbusy(this);
            }
            Thread.yield();
            
        }
        // destroy
        this.worker.destroy();
    }
    
    public void startWorker() {
        this.canRun = true;
        super.start();
    }
    
    public void stopWorker() {
        this.canRun = false;
    }
}
