package adt;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Low Zi Qing
 * @param <T>
 */
public class LinkedList<T> implements ListInterface<T>, Serializable, Iterable<T> {

    private static final long serialVersionUID = 1L;
    private Node<T> head;
    private int size;

    private static class Node<T> implements Serializable {

        private static final long serialVersionUID = 1L;
        T data;
        Node<T> next;

        Node(T d) {
            data = d;
        }
    }

    public LinkedList() {
        head = null;
        size = 0;
    }

    @Override
    public boolean add(T item) {
        Node<T> n = new Node<>(item);
        if (contains(item)) {
            return false;
        }
        if (head == null) {
            head = n;
        } else {
            Node<T> c = head;
            while (c.next != null) {
                c = c.next;
            }
            c.next = n;
        }
        size++;
        return true;
    }

    @Override
    public boolean add(int index, T item) {
        if (index < 0 || index > size) {
            return false;
        }

        Node<T> n = new Node<>(item);

        if (index == 0) {
            n.next = head;
            head = n;
        } else {
            Node<T> p = nodeAt(index - 1);
            n.next = p.next;
            p.next = n;
        }

        size++;
        return true;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node<T> r;
        if (index == 0) {
            r = head;
            head = head.next;
        } else {
            Node<T> p = nodeAt(index - 1);
            r = p.next;
            p.next = r.next;
        }
        size--;
        return r.data;
    }

    @Override
    public boolean remove(T item) {
        if (head == null) {
            return false;
        }
        if (eq(head.data, item)) {
            head = head.next;
            size--;
            return true;
        }
        Node<T> p = head, c = head.next;
        while (c != null) {
            if (eq(c.data, item)) {
                p.next = c.next;
                size--;
                return true;
            }
            p = c;
            c = c.next;
        }
        return false;
    }

    @Override
    public T get(int index) {
        return nodeAt(index).data;
    }

    @Override
    public T set(int index, T item) {
        Node<T> n = nodeAt(index);
        T old = n.data;
        n.data = item;
        return old;
    }

    @Override
    public int indexOf(T item) {
        int i = 0;
        for (Node<T> c = head; c != null; c = c.next, i++) {
            if (eq(c.data, item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        int i = 0;
        for (Node<T> c = head; c != null; c = c.next) {
            a[i++] = c.data;
        }
        return a;
    }

    private Node<T> nodeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> c = head;
        for (int i = 0; i < index; i++) {
            c = c.next;
        }
        return c;
    }

    private boolean eq(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator<>(head);
    }

    private static class CustomIterator<T> implements Iterator<T> {

        private Node<T> current;

        public CustomIterator(Node<T> head) {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> c = head;
        while (c != null) {
            sb.append(c.data);
            if (c.next != null) {
                sb.append(", ");
            }
            c = c.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
