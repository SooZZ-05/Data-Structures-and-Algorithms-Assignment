package adt;

/** Author: Teo Geok Woon */

import java.util.Iterator;

public interface QueueInterface<T> {
  Iterator<T> getIterator();
  
  void enqueue(T newEntry);
  
  T dequeue();
  
  T getFront();
  
  boolean isEmpty();
  
  void clear();

  int size();
  
  T getBack();
  
  boolean contains(T item);
  
  boolean remove(T item);
  
  int indexOf(T item);
  
  QueueInterface<T> copy();
  
  Object[] toArray();
}
