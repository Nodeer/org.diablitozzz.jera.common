package org.diablitozzz.jera.data;

import java.util.ArrayList;
import java.util.Comparator;

public interface DataUtils {
    
    static <V> DataCollection<V> empty() {
        return DataListImpl.empty();
    }
    
    static <V> DataList<V> list() {
        return DataListImpl.fromIterable(new ArrayList<>());
    }
    
    static <V> DataList<V> list(final int initSize) {
        return DataListImpl.fromIterable(new ArrayList<>(initSize));
    }

    @SafeVarargs
    static <V> DataList<V> listFromArray(final V... items) {
        return DataListImpl.fromArray(items);
    }

    static <V> DataList<V> listFromIterable(final Iterable<V> items) {
        return DataListImpl.fromIterable(items);
    }

    static <V> DataCollection<V> merge(final DataCollection<V> a, final DataCollection<V> b) {
        final DataSetImpl<V> out = new DataSetImpl<V>();
        out.addLastOrReplace(a);
        out.addLastOrReplace(b);
        return out;
    }
    
    static <V> DataSet<V> set() {
        return DataUtils.set(null);
    }
    
    static <V> DataSet<V> set(final Comparator<V> comparator) {
        return comparator == null ? new DataSetImpl<>() : new DataSetImplOrdered<>(comparator);
    }

}
