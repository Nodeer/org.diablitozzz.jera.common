package org.diablitozzz.jera.table;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.diablitozzz.jera.data.DataCollection;
import org.diablitozzz.jera.data.DataList;
import org.diablitozzz.jera.data.DataMap;
import org.diablitozzz.jera.data.DataMapImpl;
import org.diablitozzz.jera.data.DataSet;
import org.diablitozzz.jera.data.DataUtils;
import org.diablitozzz.jera.func.FuncGetBy;

public class TableIndexBasic<K, V> implements TableIndex<K, V> {
    
    private final FuncGetBy<K, V> keyGetter;
    private final Map<Object, DataSet<V>> keyToValues = new HashMap<>();
    private final DataMap<V, Object> valueToKey = new DataMapImpl<>();
    private Comparator<V> comparator;

    public TableIndexBasic(final FuncGetBy<K, V> keyGetter) {
        this(keyGetter, null);
    }
    
    public TableIndexBasic(final FuncGetBy<K, V> keyGetter, final Comparator<V> comparator) {
        this.keyGetter = keyGetter;
        this.comparator = comparator;
    }
    
    private void addIndex(final Object key, final V value) {
        if (!this.keyToValues.containsKey(key)) {
            this.keyToValues.put(key, DataUtils.set(this.comparator));
        }
        this.keyToValues.get(key).addLastOrReplace(value);
    }

    public void addOrReplace(final Iterable<V> values) {
        for (final V value : values) {
            this.addOrReplace(value);
        }
    }

    public void addOrReplace(final V value) {
        final Object key = this.makeKey(value);

        if (this.valueToKey.containsKey(value)) {
            this.removeIndex(value);
        }
        this.addIndex(key, value);
        this.valueToKey.addLastOrReplace(value, key);
    }
    
    public void addOrReplace(@SuppressWarnings("unchecked") final V... values) {
        for (final V value : values) {
            this.addOrReplace(value);
        }
    }
    
    public void clear() {
        this.keyToValues.clear();
        this.valueToKey.clear();
    }
    
    @Override
    public boolean contains(final V value) {
        return this.valueToKey.containsKey(value);
    }

    @Override
    public DataCollection<V> get(final K key) {
        final Object k = (key == null) ? TableIndex.NULL_KEY : key;
        final DataCollection<V> values = this.keyToValues.get(k);
        return values == null ? DataUtils.empty() : values;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DataCollection<K> getKeys() {
        final Set<Object> keys = this.keyToValues.keySet();
        final DataList<K> out = DataUtils.list(keys.size());
        for (final Object key : keys) {
            out.add(TableIndex.NULL_KEY.equals(key) ? null : (K) key);
        }
        return out;
    }
    
    @Override
    public boolean isEmpty() {
        return this.valueToKey.isEmpty();
    }

    private Object makeKey(final V value) {
        final Object key = this.keyGetter.invoke(value);
        return key == null ? TableIndex.NULL_KEY : key;
    }

    public void remove(final V value) {
        if (this.valueToKey.containsKey(value)) {
            this.removeIndex(value);
            this.valueToKey.remove(value);
        }
    }

    private void removeIndex(final V value) {
        final Object key = this.valueToKey.get(value);
        if (key == null) {
            return;
        }
        final DataSet<V> values = this.keyToValues.get(key);
        if (values == null) {
            return;
        }
        values.remove(value);
        if (values.isEmpty()) {
            this.keyToValues.remove(key);
        }
    }

    @Override
    public int size() {
        return this.valueToKey.size();
    }

}
