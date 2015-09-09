package org.diablitozzz.jera.table;

import org.diablitozzz.jera.data.DataCollection;

public interface TableIndex<K, V> {
    
    public static final Object NULL_KEY = new Object();

    boolean contains(V value);
    
    DataCollection<V> get(K key);

    DataCollection<K> getKeys();
    
    boolean isEmpty();
    
    int size();
}
