package adt;

import util.Comparer;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */

public class SortedLinkedList<T> implements SortedListInterface<T> {
    private static final class Node<E> {
        E data;
        Node<E> next;
        Node(E d, Node<E> n) { data = d; next = n; }
    }

    private final Comparer<T> comparer;
    private final Node<T> head = new Node<>(null, null); // sentinel
    private int size = 0;

    public SortedLinkedList(Comparer<T> comparer) {
        if (comparer == null) throw new NullPointerException("comparer");
        this.comparer = comparer;
    }

    @Override
    public boolean add(T element) {
        if (element == null) throw new NullPointerException("element");
        Node<T> prev = head;
        Node<T> cur = head.next;
        while (cur != null && comparer.compare(cur.data, element) <= 0) {
            prev = cur;
            cur = cur.next;
        }
        prev.next = new Node<>(element, cur);
        size++;
        return true;
    }

    @Override
    public T removeAt(int index) {
        rangeCheck(index);
        Node<T> prev = head;
        Node<T> cur = head.next;
        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.next;
        }
        prev.next = cur.next;
        size--;
        return cur.data;
    }

    @Override
    public T get(int index) {
        rangeCheck(index);
        Node<T> cur = head.next;
        for (int i = 0; i < index; i++) cur = cur.next;
        return cur.data;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head.next = null;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        Node<T> cur = head.next;
        int i = 0;
        while (cur != null) {
            a[i++] = cur.data;
            cur = cur.next;
        }
        return a;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException(index + " of " + size);
    }
    
    public T remove(T anEntry){
        return null;
    }
    
    public int getlength(){
        return 0;
    }
    
    public boolean isEmpty(){
        return true;
    }
    
    public boolean contains(T anEntry){
        return true;
    }
}