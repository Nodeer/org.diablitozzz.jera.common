package org.diablitozzz.jera.table;

import org.diablitozzz.jera.func.FuncGetBy;

class MockItem {
    
    public final static FuncGetBy<Object, MockItem> ID_GETTER = (e) -> {
        return e.getId();
    };

    public final static FuncGetBy<Object, MockItem> VALUE_GETTER = (e) -> {
        return e.getValue();
    };
    
    private final String id;
    private String value;

    public MockItem(final String id, final String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
