package adt;

/**
 *
 * @author Low Zi Qing
 * @param <T>
 */
public interface ListInterface<T> extends Iterable<T> {

    public boolean add(T item);

    public boolean add(int index, T item);

    public T remove(int index);

    public boolean remove(T item);

    public T get(int index);

    public T set(int index, T item);

    public int indexOf(T item);

    public boolean contains(T item);

    public int size();

    public boolean isEmpty();

    public void clear();

    public Object[] toArray();
}
