package io.tmoore.treap;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TreapNode<T extends Comparable<T>> implements Collection<T> {

    private static final Random random = new Random();
    private static final int priorityLimit = 100;

    private TreapNode<T> left;
    private TreapNode<T> right;

    /**
     * The value held by {@link this}.
     */
    private T value;

    /**
     * The priority of the node in the tree. Always increase as one moves up in the tree.
     */
    private int priority;

    TreapNode() {
        this(null, null, null);
    }

    TreapNode(T value, TreapNode<T> right, TreapNode<T> left) {
        this(value, random.nextInt(priorityLimit), right, left);
    }

    TreapNode(T value, int priority, TreapNode<T> right, TreapNode<T> left) {
        this.value = value;
        this.priority = priority;
        this.left = left;
        this.right = right;
    }

    TreapNode(T value, int priority) {
        this(value, priority, null, null);
    }

    private TreapNode(T value) {
        this(value, null, null);
    }

    @Override
    public int size() {
        return 1 + (left == null ? 0 : left.size()) + (right == null ? 0 : right.size());
    }

    @Override
    public boolean isEmpty() {
        return value == null && left == null && right == null;
    }

    @Override
    public boolean contains(Object o) {
        Objects.requireNonNull(o);
        return (o.equals(value))
               || (left != null && left.contains(o))
               || (right != null && right.contains(o));
    }

    @Override
    public Iterator<T> iterator() {
        return toList().iterator();
    }

    @Override
    public Object[] toArray() {
        return toList().toArray();
    }

    private List<T> toList() {
        ArrayList<T> collector = new ArrayList<>();
        addContentsToList(collector);
        return collector;
    }

    /**
     * Collect the contents of {@link this} and all child {@link TreapNode}s into the given list.
     *
     * @param list to collect contents into
     */
    private void addContentsToList(List<T> list) {
        if (left != null) {
            left.addContentsToList(list);
        }

        list.add(value);

        if (right != null) {
            right.addContentsToList(list);
        }
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        //noinspection SuspiciousToArrayCall
        return toList().toArray(a);
    }

    @Override
    public boolean add(T newItem) {
        Objects.requireNonNull(newItem);
        return add(newItem, null);
    }

    boolean add(T newItem, Integer priority) {
        Objects.requireNonNull(newItem);

        if (value == null) {
            value = newItem;
            return true;
        }

        if (newItem.equals(value)) {
            return false;
        }

        if (newItem.compareTo(value) < 0) {
            // Add to the left side
            if (left == null) {
                left = (priority == null) ? new TreapNode<>(newItem)
                                          : new TreapNode<>(newItem, priority);
                return true;
            } else {
                return left.add(newItem, priority);
            }
        }
        else {
            // Add to the right side
            if (right == null) {
                right = (priority == null) ? new TreapNode<>(newItem)
                                           : new TreapNode<>(newItem, priority);
                return true;
            } else {
                return right.add(newItem, priority);
            }
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || value == null || value.getClass() != o.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        T item = (T) o;

        return remove(item);
    }

    private boolean remove(T item) {
        Objects.requireNonNull(item);

        if (item.equals(value)) {
            priority = Integer.MIN_VALUE;
            return true;
        }

        return (left != null && left.remove(item))
               || (right != null && right.remove(item));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        for (T item : c) {
            changed |= add(item);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object item : c) {
            changed |= remove(item);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (T item : this) {
            if (!c.contains(item)) {
                changed |= remove(item);
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        value = null;
        left = null;
        right = null;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "Empty Treap";
        }

        return value + " (" + priority + ')';
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return false;
    }

    @Override
    public Spliterator<T> spliterator() {
        return null;
    }

    @Override
    public Stream<T> stream() {
        return null;
    }

    @Override
    public Stream<T> parallelStream() {
        return null;
    }
}

