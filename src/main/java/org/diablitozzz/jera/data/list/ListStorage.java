package org.diablitozzz.jera.data.list;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListStorage {
    private final int maxSize;
    private volatile int size = 0;
    private ListNode first = null;
    private ListNode last = null;
    
    /**
     * Конструктор, который создаёт список, имеющий неограниченную длину
     */
    public ListStorage() {
        this(0);
    }
    
    /**
     * Конструктор
     *
     * @param maxSize
     *            максимальный размер списка
     */
    public ListStorage(final int maxSize) {
        this.maxSize = maxSize;
    }
    
    /**
     * Добавляет элемент в начало списка
     *
     * @param node
     *            элемент
     */
    public void addFirst(final ListNode node) throws ListException {
        if (this.isFull()) {
            throw new ListException("Storage is full");
        }
        if (node.storage != null && node.storage != this) {
            throw new ListException("The node already is in other storage");
        }
        if (this.isEmpty()) {
            this.last = node;
        } else {
            this.first.prev = node;
            node.next = this.first;
        }
        this.first = node;
        node.storage = this;
        this.size++;
    }
    
    /**
     * Добавляет элемент в конец списка
     *
     * @param node
     *            элемент
     */
    public void addLast(final ListNode node) throws ListException {
        if (this.isFull()) {
            throw new ListException("Storage is full");
        }
        if (node.storage != null && node.storage != this) {
            throw new ListException("The node already is in other storage");
        }
        if (this.isEmpty()) {
            this.first = node;
        } else {
            this.last.next = node;
            node.prev = this.last;
        }
        this.last = node;
        node.storage = this;
        this.size++;
    }
    
    /**
     * Очищает список
     */
    public void clear() {
        while (!this.isEmpty()) {
            this.deleteLast();
        }
    }
    
    /**
     * Проверяет находится ли данный нод в данном сторедже
     */
    public boolean contains(final ListNode node) {
        return node.storage == this;
    }
    
    /**
     * Удаляет элемент из списка
     *
     * @param node
     *            элемент
     * @throws ListException
     */
    public void delete(final ListNode node) {
        if (this.isEmpty()) {
            return;
        }
        if (!this.contains(node)) {
            return;
        }
        node.storage = null;
        if (node.prev == null && node.next == null && this.first != node && this.last != node) {
            return;
        } else if (node.prev == null && node.next != null) {
            node.next.prev = null;
        } else if (node.prev != null && node.next == null) {
            node.prev.next = null;
        } else if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        if (this.first == node) {
            this.first = node.next;
        }
        if (this.last == node) {
            this.last = node.prev;
        }
        node.prev = null;
        node.next = null;
        node.storage = null;
        this.size--;
    }
    
    /**
     * Удаляет первый элемент списка
     */
    public void deleteFirst() {
        if (!this.isEmpty()) {
            this.delete(this.first);
        }
    }
    
    /**
     * Удаляет последний элемент списка
     */
    public void deleteLast() {
        if (!this.isEmpty()) {
            this.delete(this.last);
        }
    }
    
    /**
     * Деструктор, который очищает список
     */
    @Override
    protected void finalize() {
        
        this.clear();
    }
    
    /**
     * Возвращает первый элемент в списке
     */
    public ListNode getFirst() {
        return this.first;
    }
    
    /**
     * Возвращает последний элемент в списке
     */
    public ListNode getLast() {
        return this.last;
    }
    
    /**
     * Возвращает кол-во элементов в списке
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Возвращает, является ли список пустым
     */
    public boolean isEmpty() {
        return (this.first == null && this.last == null);
    }
    
    /**
     * Возвращает, является ли список полным
     */
    public boolean isFull() {
        return this.maxSize != 0 && this.size >= this.maxSize;
    }
    
    public List<ListNode> toList() {
        List<ListNode> out;
        if (this.size > 0) {
            out = new ArrayList<ListNode>(this.size);
        } else {
            out = new LinkedList<ListNode>();
        }
        
        ListNode current = this.first;
        while (current != null) {
            out.add(current);
            current = current.next;
        }
        return out;
    }
}
