package org.diablitozzz.jera.cache;

public interface LazyCache<C, V> extends AutoCloseable {
    
    public V get(C condition);
    
}
