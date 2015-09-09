package org.diablitozzz.jera.data.list;

public class ListNode {
    
    protected ListNode next;
    protected ListNode prev;
    protected ListStorage storage;
    private Object value;
    
    @Override
    protected void finalize() {
        this.next = null;
        this.prev = null;
        this.storage = null;
        this.value = null;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public void setValue(final Object value) {
        this.value = value;
    }
}
