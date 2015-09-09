package org.diablitozzz.jera.concurrency.async;

public interface AsyncTask extends Runnable {
    
    @Override
    boolean equals(Object obj);
    
    @Override
    int hashCode();
    
    @Override
    void run();
    
}
