package adt;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class HashMap<K, V> implements Iterable<HashMap.Entry<K, V>> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    private Entry<K, V>[] table;
    private int size;
    private final float loadFactor;
    
    @SuppressWarnings("unchecked")
    public HashMap() {
        this.table = new Entry[DEFAULT_CAPACITY];
        this.size = 0;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }
    
    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        
        this.table = new Entry[initialCapacity];
        this.size = 0;
        this.loadFactor = loadFactor;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public V get(K key) {
        if (key == null) {
            return getForNullKey();
        }
        
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && (key.equals(e.key))) {
                return e.value;
            }
        }
        
        return null;
    }
    
    private V getForNullKey() {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                return e.value;
            }
        }
        return null;
    }
    
    public V put(K key, V value) {
        if (key == null) {
            return putForNullKey(value);
        }
        
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && (key.equals(e.key))) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        
        addEntry(key, value, hash, index);
        return null;
    }
    
    private V putForNullKey(V value) {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        
        addEntry(null, value, 0, 0);
        return null;
    }
    
    public V remove(K key) {
        if (key == null) {
            return removeForNullKey();
        }
        
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Entry<K, V> prev = table[index];
        Entry<K, V> e = prev;
        
        while (e != null) {
            Entry<K, V> next = e.next;
            if (e.hash == hash && (key.equals(e.key))) {
                if (prev == e) {
                    table[index] = next;
                } else {
                    prev.next = next;
                }
                size--;
                return e.value;
            }
            prev = e;
            e = next;
        }
        
        return null;
    }
    
    private V removeForNullKey() {
        Entry<K, V> prev = table[0];
        Entry<K, V> e = prev;
        
        while (e != null) {
            Entry<K, V> next = e.next;
            if (e.key == null) {
                if (prev == e) {
                    table[0] = next;
                } else {
                    prev.next = next;
                }
                size--;
                return e.value;
            }
            prev = e;
            e = next;
        }
        
        return null;
    }
    
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }
    
    private void addEntry(K key, V value, int hash, int index) {
        Entry<K, V> e = table[index];
        table[index] = new Entry<>(hash, key, value, e);
        
        if (size++ >= table.length * loadFactor) {
            resize(2 * table.length);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        
        if (oldCapacity == Integer.MAX_VALUE) {
            return;
        }
        
        Entry<K, V>[] newTable = new Entry[newCapacity];
        
        for (int i = 0; i < oldCapacity; i++) {
            Entry<K, V> e = oldTable[i];
            if (e != null) {
                oldTable[i] = null;
                do {
                    Entry<K, V> next = e.next;
                    int index = indexFor(e.hash, newCapacity);
                    e.next = newTable[index];
                    newTable[index] = e;
                    e = next;
                } while (e != null);
            }
        }
        
        table = newTable;
    }
    
    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
    
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }
    
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashMapIterator();
    }
    
    private class HashMapIterator implements Iterator<Entry<K, V>> {
        private int index = 0;
        private Entry<K, V> current = null;
        private Entry<K, V> next = null;
        
        public HashMapIterator() {
            if (size > 0) {
                while (index < table.length && (next = table[index++]) == null) {
                    // Find the first non-empty bucket
                }
            }
        }
        
        @Override
        public boolean hasNext() {
            return next != null;
        }
        
        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            current = next;
            next = current.next;
            
            if (next == null) {
                while (index < table.length && (next = table[index++]) == null) {
                    // Find the next non-empty bucket
                }
            }
            
            return current;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Entry<K, V> next;
        
        Entry(int hash, K key, V value, Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
        
        public K getKey() {
            return key;
        }
        
        public V getValue() {
            return value;
        }
        
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}