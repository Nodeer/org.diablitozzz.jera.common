package org.diablitozzz.jera.data;

import java.util.Collection;

public interface DataList<V> extends DataListImmutable<V> {

    void add(final V value);

    void addAll(final DataCollection<V> value);

    void addAll(final Collection<V> value);

    void clear();
    
    void remove(int index);

    void remove(V value);
}
