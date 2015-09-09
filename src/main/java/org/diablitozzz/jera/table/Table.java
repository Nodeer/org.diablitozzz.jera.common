package org.diablitozzz.jera.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.diablitozzz.jera.data.DataCollection;
import org.diablitozzz.jera.data.DataSet;
import org.diablitozzz.jera.data.DataUtils;
import org.diablitozzz.jera.func.FuncGetBy;

public class Table<V> {

    private final List<TableIndexBasic<?, V>> indexList = new ArrayList<>();
    private final DataSet<V> items = DataUtils.set();
    private final int maxSize;

    public Table() {
        this(0);
    }
    
    public Table(final int maxSize) {
        this.maxSize = maxSize;
    }

    public void addOrReplace(final Iterable<V> values) {
        if (values == null) {
            return;
        }
        for (final V value : values) {
            this.addOrReplace(value);
        }
    }
    
    public void addOrReplace(final V value) {
        if (value == null) {
            return;
        }
        this.items.addLastOrReplace(value);
        if (this.maxSize > 0 && this.items.size() == this.maxSize + 1) {
            this.remove(this.items.getFirst());
        }
        for (final TableIndexBasic<?, V> index : this.indexList) {
            index.addOrReplace(value);
        }
    }
    
    public void addOrReplace(@SuppressWarnings("unchecked") final V... values) {
        if (values == null) {
            return;
        }
        for (final V value : values) {
            this.addOrReplace(value);
        }
    }
    
    public void clear() {
        this.items.clear();
        for (final TableIndexBasic<?, V> index : this.indexList) {
            index.clear();
        }
    }

    public boolean contains(final V value) {
        if (value == null) {
            return false;
        }
        return this.items.contains(value);
    }
    
    public <K> TableIndex<K, V> createIndex(final FuncGetBy<K, V> keyGetter) {
        final TableIndexBasic<K, V> index = new TableIndexBasic<>(keyGetter);
        this.indexList.add(index);
        return index;
    }
    
    public <K> TableIndex<K, V> createIndex(final FuncGetBy<K, V> keyGetter, final Comparator<V> comparator) {
        final TableIndexBasic<K, V> index = new TableIndexBasic<>(keyGetter, comparator);
        this.indexList.add(index);
        return index;
    }
    
    public DataCollection<V> getAll() {
        return this.items;
    }
    
    public void remove(final V value) {
        if (value == null) {
            return;
        }
        this.items.remove(value);
        for (final TableIndexBasic<?, V> index : this.indexList) {
            index.remove(value);
        }
    }
    
}
