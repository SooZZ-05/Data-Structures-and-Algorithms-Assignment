package adt;

/** Author: Teo Geok Woon */

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;

public class ArrayQueue<T> implements QueueInterface<T>, Serializable {

  private T[] array;
  private static final int FRONT_INDEX = 0;
  private int backIndex;
  private static final int DEFAULT_CAPACITY = 50;

  public ArrayQueue() { this(DEFAULT_CAPACITY); }

  @SuppressWarnings("unchecked")
  public ArrayQueue(int initialCapacity) {
    array = (T[]) new Object[initialCapacity];
    backIndex = -1;
  }

  @Override
  public void enqueue(T newEntry) {
    if (!isArrayFull()) {
      backIndex++;
      array[backIndex] = newEntry;
    }
  }

  @Override
  public T getFront() {
    return isEmpty() ? null : array[FRONT_INDEX];
  }

  @Override
  public T dequeue() {
    if (isEmpty()) return null;
    T front = array[FRONT_INDEX];
    for (int i = FRONT_INDEX; i < backIndex; ++i) array[i] = array[i + 1];
    array[backIndex] = null;
    backIndex--;
    return front;
  }

  @Override
  public boolean isEmpty() { return 0 > backIndex; }

  @Override
  public void clear() {
    if (!isEmpty()) {
      for (int i = 0; i <= backIndex; i++) array[i] = null;
      backIndex = -1;
    }
  }

  private boolean isArrayFull() { return backIndex == array.length - 1; }

  @Override
  public Iterator<T> getIterator() { return new ArrayQueueIterator(); }

  private class ArrayQueueIterator implements Iterator<T> {
    private int nextIndex = 0;

    @Override public boolean hasNext() { return nextIndex <= backIndex; }

    @Override
    public T next() {
      if (!hasNext()) throw new NoSuchElementException();
      return array[nextIndex++];
    }
  }

  @Override public int size() { return backIndex + 1; }
  @Override public T getBack() { return isEmpty() ? null : array[backIndex]; }

  @SuppressWarnings("unchecked")
  public void ensureCapacity(int minCapacity) {
    if (minCapacity <= array.length) return;
    int newCap = Math.max(minCapacity, array.length * 2);
    T[] newArray = (T[]) new Object[newCap];
    for (int i = 0; i <= backIndex; i++) newArray[i] = array[i];
    array = newArray;
  }

  public void trimToSize() {
    if (backIndex + 1 < array.length) {
      @SuppressWarnings("unchecked")
      T[] newArray = (T[]) new Object[backIndex + 1];
      for (int i = 0; i <= backIndex; i++) newArray[i] = array[i];
      array = newArray;
    }
  }

  public void enqueueAll(T[] items) {
    if (items == null) return;
    ensureCapacity(size() + items.length);
    for (T item : items) if (item != null) enqueue(item);
  }

  @Override public boolean contains(T item) {
    for (int i = 0; i <= backIndex; i++) if (Objects.equals(array[i], item)) return true;
    return false;
  }

  @Override
  public QueueInterface<T> copy() {
    ArrayQueue<T> copy = new ArrayQueue<>(array.length);
    for (int i = 0; i <= backIndex; i++) copy.enqueue(array[i]);
    return copy;
  }

  @Override
  public Object[] toArray() {
    Object[] result = new Object[size()];
    for (int i = 0; i <= backIndex; i++) result[i] = array[i];
    return result;
  }

  @Override
  public boolean remove(T item) {
    for (int i = 0; i <= backIndex; i++) {
      if (Objects.equals(array[i], item)) {
        for (int j = i; j < backIndex; j++) array[j] = array[j + 1];
        array[backIndex] = null;
        backIndex--;
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOf(T item) {
    for (int i = 0; i <= backIndex; i++) if (Objects.equals(array[i], item)) return i;
    return -1;
  }

  public int capacity() { return array.length; }
}
