package org.diablitozzz.jera.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;

public class DataSetImpl<V> implements DataSet<V> {

    private final LinkedHashSet<V> values = new LinkedHashSet<>();
    private V first;
    private V last;
    
    @Override
    public void addLastOrReplace(final DataCollection<V> value) {
        for (final V v : value) {
            this.addLastOrReplace(v);
        }
    }
    
    @Override
    public void addLastOrReplace(final V value) {
        if (this.values.contains(value)) {
            this.values.remove(value);
        } else {
            if (this.first == null) {
                this.first = value;
            }
            this.last = value;
        }
        this.values.add(value);
    }

    @Override
    public void clear() {
        this.values.clear();
        this.first = null;
        this.last = null;
    }

    @Override
    public boolean contains(final V key) {
        return this.values.contains(key);
    }
    
    @Override
    public DataCollection<V> filterAndCopy(final Function<V, Boolean> filter) {
        final DataList<V> out = DataUtils.list(this.size());
        for (final V value : this.values) {
            if (filter.apply(value)) {
                out.add(value);
            }
        }
        return out;
    }

    @Override
    public V get(final int index) {
        int i = 0;
        for (final V v : this.values) {
            if (index == i) {
                return v;
            }
            i++;
        }
        return null;
    }

    @Override
    public V getFirst() {
        return this.first;
    }
    
    @Override
    public DataCollection<V> getFirstAndCopy(final int size) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V getLast() {
        return this.last;
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
        return this.values.iterator();
    }

    @Override
    public void remove(final V value) {
        if (this.values.isEmpty()) {
            return;
        }
        this.values.remove(value);
        if (this.values.isEmpty()) {
            this.first = null;
            this.last = null;
        } else {

            if (this.first.equals(value)) {
                this.first = this.values.iterator().next();
            }
            if (this.last.equals(value)) {
                final Iterator<V> iterator = this.values.iterator();
                while (iterator.hasNext()) {
                    this.last = iterator.next();
                }
            }
        }
    }

    @Override
    public DataCollection<V> reverseAndCopy() {
        final List<V> out = new ArrayList<>(this.values);
        Collections.reverse(out);
        return DataUtils.listFromIterable(out);
    }

    @Override
    public int size() {
        return this.values.size();
    }

    @Override
    public DataCollection<V> sortAndCopy(final Comparator<V> comparator) {
        final List<V> out = new ArrayList<>(this.values);
        Collections.sort(out, comparator);
        return DataUtils.listFromIterable(out);
    }
    
    @Override
    public V[] toArray(final V[] array) {
        return this.values.toArray(array);
    }
    
    @Override
    public List<V> toList() {
        return new ArrayList<>(this.values);
    }
}
