package org.diablitozzz.jera.data;

public interface DataMap<K, V> extends DataMapImmutable<K, V> {

    void addLastOrReplace(K key, V value);

    void clear();

    void remove(K key);
}
