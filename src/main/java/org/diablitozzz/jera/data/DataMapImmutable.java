package org.diablitozzz.jera.data;

public interface DataMapImmutable<K, V> {

    boolean containsKey(final K key);

    V get(final K key);

    K getFirstKey();
    
    DataCollection<K> getKeys();
    
    K getLastKey();
    
    boolean isEmpty();

    int size();
    
}
