package org.diablitozzz.jera.data;

public interface DataSet<V> extends DataSetImmutable<V> {
    
    void addLastOrReplace(final DataCollection<V> value);

    void addLastOrReplace(final V value);

    void clear();
    
    void remove(V value);

}
