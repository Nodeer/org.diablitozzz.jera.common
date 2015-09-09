package org.diablitozzz.jera.data;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public interface DataCollection<V> extends Iterable<V> {
    
    boolean contains(final V value);

    DataCollection<V> filterAndCopy(Function<V, Boolean> filter);
    
    V get(final int index);

    V getFirst();
    
    DataCollection<V> getFirstAndCopy(int size);
    
    V getLast();

    DataCollection<V> getLastAndCopy(int size);

    boolean isEmpty();
    
    @Override
    Iterator<V> iterator();

    DataCollection<V> reverseAndCopy();

    int size();
    
    DataCollection<V> sortAndCopy(Comparator<V> comparator);

    V[] toArray(V[] array);
    
    List<V> toList();
}
