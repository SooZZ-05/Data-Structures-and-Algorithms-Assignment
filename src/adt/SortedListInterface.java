package adt;

/**
 *
 * @author Sebastian Tan & SooZZ
 * @param <T>
 */
public interface SortedListInterface<T> {

    public boolean add(T newEntry);

    public T removeAt(int index);

    public T remove(T anEntry);

    public T get(int index);

    public int size();

    public int getlength();

    public void clear();

    public boolean isEmpty();

    public boolean contains(T anEntry);

    Object[] toArray();
}
