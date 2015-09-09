package org.diablitozzz.jera.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class DataListImpl<V> implements DataList<V> {

    public static <V> DataListImpl<V> empty() {
        return new DataListImpl<>(new ArrayList<>());
    }

    public static <V> DataListImpl<V> fromArray(@SuppressWarnings("unchecked") final V... items) {
        if (items == null) {
            return DataListImpl.empty();
        }
        final List<V> data = new ArrayList<>(items.length);
        for (final V item : items) {
            data.add(item);
        }
        return new DataListImpl<>(data);
    }
    
    public static <V> DataListImpl<V> fromIterable(final Iterable<V> items) {
        if (items == null) {
            return DataListImpl.empty();
        } else if (items instanceof Collection) {
            return new DataListImpl<>(new ArrayList<>((Collection<V>) items));
        } else if (items instanceof DataCollection) {
            return new DataListImpl<>(((DataCollection<V>) items).toList());
        } else {
            final List<V> data = new ArrayList<>();
            for (final V item : items) {
                data.add(item);
            }
            return new DataListImpl<V>(data);
        }
    }

    private final List<V> data;

    private DataListImpl(final List<V> data) {
        this.data = data;
    }

    @Override
    public void add(final V value) {
        this.data.add(value);
    }
    
    @Override
    public void addAll(final DataCollection<V> value) {
        this.data.addAll(value.toList());
    }
    
    @Override
    public void addAll(final Collection<V> value) {
        this.data.addAll(value);
    }
    
    @Override
    public void clear() {
        this.data.clear();
    }
    
    @Override
    public boolean contains(final V value) {
        return this.data.contains(value);
    }

    @Override
    public DataCollection<V> filterAndCopy(final Function<V, Boolean> filter) {
        final DataList<V> out = DataUtils.list(this.size());
        for (final V value : this.data) {
            if (filter.apply(value)) {
                out.add(value);
            }
        }
        return out;
    }
    
    @Override
    public V get(final int index) {
        return this.data.get(index);
    }
    
    @Override
    public V getFirst() {
        if (this.data.isEmpty()) {
            return null;
        }
        return this.data.get(0);

    }
    
    @Override
    public DataCollection<V> getFirstAndCopy(final int size) {
        final DataList<V> out = DataUtils.list(size);
        final Iterator<V> iterator = this.data.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            if (i == size) {
                break;
            }
            out.add(iterator.next());
        }
        return out;
    }
    
    @Override
    public V getLast() {
        if (this.data.isEmpty()) {
            return null;
        }
        return this.data.get(this.data.size() - 1);
    }
    
    @Override
    public DataCollection<V> getLastAndCopy(final int size) {
        final int start = Math.max(0, this.data.size() - size);
        final DataList<V> out = DataUtils.list(size);
        final Iterator<V> iterator = this.data.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i >= start) {
                out.add(iterator.next());
            }
            i++;
        }
        return out.reverseAndCopy();
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }
    
    @Override
    public Iterator<V> iterator() {

        final Iterator<V> iterator = this.data.iterator();
        return new Iterator<V>() {
            
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public V next() {
                return iterator.next();
            }
        };
    }
    
    @Override
    public void remove(final int index) {
        this.data.remove(index);

    }
    
    @Override
    public void remove(final V value) {
        this.data.remove(value);
    }
    
    @Override
    public DataCollection<V> reverseAndCopy() {
        final List<V> out = new ArrayList<>(this.data);
        Collections.reverse(out);
        return new DataListImpl<>(out);
    }
    
    @Override
    public int size() {
        return this.data.size();
    }
    
    @Override
    public DataCollection<V> sortAndCopy(final Comparator<V> comparator) {
        final List<V> out = new ArrayList<>(this.data);
        Collections.sort(out, comparator);
        return new DataListImpl<>(out);
    }
    
    @Override
    public V[] toArray(final V[] array) {
        return this.data.toArray(array);
    }
    
    @Override
    public List<V> toList() {
        return new ArrayList<>(this.data);
    }
}
