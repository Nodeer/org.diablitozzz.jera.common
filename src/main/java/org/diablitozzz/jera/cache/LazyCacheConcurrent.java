package org.diablitozzz.jera.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract public class LazyCacheConcurrent<C, V> implements LazyCache<C, V> {
    
    private final Map<Object, V> storage = new ConcurrentHashMap<Object, V>();
    
    @Override
    public void close() throws Exception {
        this.storage.clear();
    }
    
    abstract protected V createValue(C condition);
    
    @Override
    public V get(final C condition) {
        
        final Object key = this.toKey(condition);
        
        if (!this.storage.containsKey(key)) {
            final V value = this.createValue(condition);
            this.storage.put(key, value);
            return value;
        }
        return this.storage.get(key);
    }
    
    protected Object toKey(final C condition) {
        return condition;
    }
    
}
