package adt;

/**
 *
 * @author Gan Jun Wei
 */
public interface MapADT<K, V> {

    int size();

    boolean isEmpty();

    boolean containsKey(K key);

    V get(K key);

    void put(K key, V value);

    V remove(K key);

    void clear();

    Iterable<K> keys();

    Iterable<V> values();

    Iterable<MapADT.Entry<K, V>> entries();

    interface Entry<K, V> {

        K getKey();

        V getValue();

        void setValue(V value);
    }
}
