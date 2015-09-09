package org.diablitozzz.jera.concurrency.threadpool;

public interface ThreadPoolWorker {
    
    public void destroy();
    
    public void init();
    
    public void service(Object message);
    
}
