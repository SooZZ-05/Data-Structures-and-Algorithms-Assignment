package adt;

/**
 *
 * @author Gan Jun Wei
 */
import java.util.Iterator;

// List ADT Interface
public interface ListADT<E> extends Iterable<E> {

    int size();

    boolean isEmpty();

    boolean contains(E element);

    void add(E element);

    void add(int index, E element);

    E get(int index);

    E set(int index, E element);

    E remove(int index);

    boolean remove(E element);

    int indexOf(E element);

    void clear();

    Iterator<E> iterator();

    Object[] toArray();
}
