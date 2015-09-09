package org.diablitozzz.jera.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

public class DataSetImplOrdered<V> implements DataSet<V> {

    private final TreeMap<V, Integer> values;
    
    public DataSetImplOrdered(final Comparator<V> comparator) {
        this.values = new TreeMap<>(comparator);
    }
    
    @Override
    public void addLastOrReplace(final DataCollection<V> value) {
        for (final V v : value) {
            this.addLastOrReplace(v);
        }
    }
    
    @Override
    public void addLastOrReplace(final V value) {
        if (this.values.containsKey(value)) {
            this.values.remove(value);
        }
        this.values.put(value, 1);
    }

    @Override
    public void clear() {
        this.values.clear();
    }

    @Override
    public boolean contains(final V key) {
        return this.values.containsKey(key);
    }
    
    @Override
    public DataCollection<V> filterAndCopy(final Function<V, Boolean> filter) {
        final DataList<V> out = DataUtils.list(this.size());
        for (final V value : this.values.keySet()) {
            if (filter.apply(value)) {
                out.add(value);
            }
        }
        return out;
    }

    @Override
    public V get(final int index) {
        int i = 0;
        for (final V v : this.values.keySet()) {
            if (index == i) {
                return v;
            }
            i++;
        }
        return null;
    }

    @Override
    public V getFirst() {
        return this.values.isEmpty() ? null : this.values.firstKey();
    }
    
    @Override
    public DataCollection<V> getFirstAndCopy(final int size) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public V getLast() {
        return this.values.isEmpty() ? null : this.values.lastKey();
    }

    @Override
    public DataCollection<V> getLastAndCopy(final int size) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    @Override
    public Iterator<V> iterator() {
        return this.values.keySet().iterator();
    }

    @Override
    public void remove(final V value) {
        this.values.remove(value);
    }

    @Override
    public DataCollection<V> reverseAndCopy() {
        final List<V> out = new ArrayList<>(this.values.keySet());
        Collections.reverse(out);
        return DataUtils.listFromIterable(out);
    }

    @Override
    public int size() {
        return this.values.size();
    }

    @Override
    public DataCollection<V> sortAndCopy(final Comparator<V> comparator) {
        final List<V> out = new ArrayList<>(this.values.keySet());
        Collections.sort(out, comparator);
        return DataUtils.listFromIterable(out);
    }
    
    @Override
    public V[] toArray(final V[] array) {
        return this.values.keySet().toArray(array);
    }
    
    @Override
    public List<V> toList() {
        return new ArrayList<>(this.values.keySet());
    }

}
