package adt;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 *
 * @author Sebastian Tan
 * @param <T>
 */
public class SortedSkipList<T extends Comparable<? super T>> implements SortedListInterface<T>, Iterable<T>, Serializable {

    private Node head;
    private Node tail;
    private T negInfinity;  // Sentinel for negative infinity
    private T posInfinity;  // Sentinel for positive infinity
    private int heightOfSkipList = 0;
    private int numberOfEntries = 0;
    public Random random = new Random();

    public SortedSkipList(T negInfinity, T posInfinity) {
        this.negInfinity = negInfinity;
        this.posInfinity = posInfinity;
        head = new Node(negInfinity);
        tail = new Node(posInfinity);
        head.next = tail;
        tail.prev = head;
    }

    @SuppressWarnings("unchecked")
    public SortedSkipList() {
        this.negInfinity = (T) getMinValue();
        this.posInfinity = (T) getMaxValue();
        head = new Node(negInfinity);
        tail = new Node(posInfinity);
        head.next = tail;
        tail.prev = head;
    }

    public boolean add(T newEntry) {
        Node position = search(newEntry);
        Node q;

        int level = -1;
        int numberOfHeads = -1;

        if (position.key.compareTo(newEntry) == 0) {
            return false;//Entry already exists
        }

        do {
            numberOfHeads++;
            level++;
            canIncreaseLevel(level);
            q = position;
            while (position.above == null) {
                position = position.prev;
            }
            position = position.above;
            q = insertAfterAbove(position, q, newEntry);
        } while (random.nextBoolean() == true);
        numberOfEntries++;
        return true;
    }

    public T remove(T anEntry) {
        Node nodeToBeRemoved = search(anEntry);

        if (nodeToBeRemoved.key.compareTo(anEntry) != 0) {
            return null; //Entry not found
        }
        removeReferencesToNode(nodeToBeRemoved);

        while (nodeToBeRemoved != null) {
            removeReferencesToNode(nodeToBeRemoved);

            if (nodeToBeRemoved.above != null) {
                nodeToBeRemoved = nodeToBeRemoved.above;
            } else {
                break;
            }
        }

        numberOfEntries--;
        return nodeToBeRemoved.key;
    }

    public void clear() {
        heightOfSkipList = 0;
        numberOfEntries = 0;
        head = new Node(negInfinity);
        tail = new Node(posInfinity);
        head.next = tail;
        tail.prev = head;
    }

    public boolean contains(T anEntry) {
        Node position = search(anEntry);
        return position.key.compareTo(anEntry) == 0;
    }

    public int getLength() {
        return numberOfEntries;
    }

    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    public void printList() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nSkipList starting with top-left most node.\n");

        Node starting = head;

        Node highestLevel = starting;
        int level = heightOfSkipList;

        while (highestLevel != null) {
            sb.append("\nLevel: " + level + "\n");

            while (starting != null) {
                sb.append(starting.key);

                if (starting.next != null) {
                    sb.append(" : ");
                }

                starting = starting.next;
            }
            highestLevel = highestLevel.below;
            starting = highestLevel;
            level--;
        }
        System.out.println(sb.toString());
    }

    public void printContents() {
        StringBuilder sb = new StringBuilder();

        Node current = getFirstDataNode();

        while (current != null && current != tail && current.key != posInfinity) {
            sb.append(current.key);

            if (current.next != null && current.next != tail && current.next.key != posInfinity) {
                sb.append("\n");
            }

            current = current.next;
        }

        System.out.println(sb.toString());
    }

    // Helper methods to get min/max values for common types
    private Comparable<?> getMinValue() {
        return Integer.MIN_VALUE;
    }

    private Comparable<?> getMaxValue() {
        return Integer.MAX_VALUE;
    }

    public Node search(T anEntry) {
        Node n = head;

        while (n.below != null) {
            n = n.below;
            while (anEntry.compareTo(n.next.key) >= 0) {
                n = n.next;
            }
        }
        return n;
    }

    public T get(T anEntry) {
        Node n = head;

        while (n.below != null) {
            n = n.below;
            while (anEntry.compareTo(n.next.key) >= 0) {
                n = n.next;
            }
        }
        return n.key;
    }

    private void removeReferencesToNode(Node nodeToBeRemoved) {
        Node afterNodeToBeRemoved = nodeToBeRemoved.next;
        Node beforeNodeToBeRemoved = nodeToBeRemoved.prev;

        beforeNodeToBeRemoved.next = afterNodeToBeRemoved;
        afterNodeToBeRemoved.prev = beforeNodeToBeRemoved;
    }

    private void canIncreaseLevel(int level) {
        if (level >= heightOfSkipList) {
            heightOfSkipList++;
            addEmptyLevel();
        }
    }

    private void addEmptyLevel() {
        Node newHeadNode = new Node(negInfinity);
        Node newTailNode = new Node(posInfinity);

        newHeadNode.next = newTailNode;
        newHeadNode.below = head;
        newTailNode.prev = newHeadNode;
        newTailNode.below = tail;

        head.above = newHeadNode;
        tail.above = newTailNode;

        head = newHeadNode;
        tail = newTailNode;
    }

    private Node insertAfterAbove(Node position, Node q, T key) {
        Node newNode = new Node(key);
        Node nodeBeforeNewNode = position.below.below;

        setBeforeAfterReferences(q, newNode);
        setAboveAndBelowReferences(position, key, newNode, nodeBeforeNewNode);

        return newNode;
    }

    private void setBeforeAfterReferences(Node q, Node newNode) {
        newNode.next = q.next;
        newNode.prev = q;
        q.next.prev = newNode;
        q.next = newNode;
    }

    private void setAboveAndBelowReferences(Node position, T key, Node newNode, Node nodeBeforeNewNode) {
        if (nodeBeforeNewNode != null) {
            while (true) {
                if (nodeBeforeNewNode.next.key.compareTo(key) != 0) {
                    nodeBeforeNewNode = nodeBeforeNewNode.next;
                } else {
                    break;
                }
            }

            newNode.below = nodeBeforeNewNode.next;
            nodeBeforeNewNode.next.above = newNode;
        }

        if (position != null) {
            if (position.next.key.compareTo(key) == 0) {
                newNode.above = position.next;
            }
        }
    }

    private Node getFirstDataNode() {
        Node current = head;
        // Go to bottom level
        while (current.below != null) {
            current = current.below;
        }
        // Skip negative infinity sentinel
        return current.next;
    }

    @Override
    public int getlength() {
        return numberOfEntries;
    }

    @Override
    public Iterator<T> iterator() {
        return new SkipListIterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    private class SkipListIterator implements Iterator<T> {

        private Node current;

        public SkipListIterator() {
            current = getFirstDataNode();
        }

        @Override
        public boolean hasNext() {
            return current != null
                    && current != tail
                    && !current.key.equals(posInfinity);
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.key;
            current = current.next;
            return data;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove not supported");
        }
    }

    private class Node implements Serializable {

        private Node above;
        private Node below;
        private Node next;
        private Node prev;
        private T key;

        public Node(T key) {
            this.key = key;
            this.above = null;
            this.below = null;
            this.next = null;
            this.prev = null;
        }
    }

    public T removeAt(int index) {
        if (index < 0 || index >= numberOfEntries) {
            return null;
        }

        Node current = getFirstDataNode();
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        T removedData = current.key;
        remove(removedData);
        return removedData;
    }

    public T get(int index) {
        if (index < 0 || index >= numberOfEntries) {
            return null;
        }

        Node current = getFirstDataNode();
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.key;
    }

    public int size() {
        return numberOfEntries;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[numberOfEntries];
        Node current = getFirstDataNode();
        int index = 0;

        while (current != null && current != tail && !current.key.equals(posInfinity)) {
            array[index++] = current.key;
            current = current.next;
        }

        return array;
    }

}
