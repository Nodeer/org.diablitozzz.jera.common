package org.diablitozzz.jera.data;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class DataMapImpl<K, V> implements DataMap<K, V> {
    
    private final LinkedHashMap<K, V> values = new LinkedHashMap<>();
    private K firstKey;
    private K lastKey;

    @Override
    public void addLastOrReplace(final K key, final V value) {
        if (this.values.containsKey(key)) {
            this.values.remove(key);
        } else {
            if (this.firstKey == null) {
                this.firstKey = key;
            }
            this.lastKey = key;
        }
        this.values.put(key, value);
    }

    @Override
    public void clear() {
        this.values.clear();
        this.firstKey = null;
        this.lastKey = null;
    }
    
    @Override
    public boolean containsKey(final K key) {
        return this.values.containsKey(key);
    }
    
    @Override
    public V get(final K key) {
        return this.values.get(key);
    }
    
    @Override
    public K getFirstKey() {
        return this.firstKey;
    }

    @Override
    public DataCollection<K> getKeys() {
        return DataUtils.listFromIterable(this.values.keySet());
    }

    @Override
    public K getLastKey() {
        return this.lastKey;
    }

    @Override
    public boolean isEmpty() {
        return this.values.isEmpty();
    }
    
    @Override
    public void remove(final K key) {
        if (this.values.isEmpty()) {
            return;
        }
        this.values.remove(key);
        if (this.firstKey.equals(key)) {
            this.firstKey = this.values.keySet().iterator().next();
        }
        if (this.lastKey.equals(key)) {

            final Iterator<K> iterator = this.values.keySet().iterator();
            while (iterator.hasNext()) {
                this.lastKey = iterator.next();
            }
        }
    }

    @Override
    public int size() {
        return this.values.size();
    }

}
