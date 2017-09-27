package io.tmoore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Just a binary tree with extra data right now.
 */
public class Treap<T extends Comparable<T>> implements Collection<T> {
    private static final Random random = new Random();

    private Treap<T> left;
    private Treap<T> right;

    /**
     * CONSTRAINT: Never {@code null} outside of constructors.
     */
    private PriorityTuple<T> tuple;

    public Treap() {
        this(null, null, null);
    }

    private Treap(T value, Treap<T> right, Treap<T> left) {
        tuple = new PriorityTuple<>(value);
        this.left = left;
        this.right = right;
    }

    public Treap(T value, int priority, Treap<T> right, Treap<T> left) {
        tuple = new PriorityTuple<>(value, priority);
        this.left = left;
        this.right = right;
    }

    private Treap(T value) {
        this(value, null, null);
    }

    public Treap(T value, int priority) {
        this(value, priority, null, null);
    }

    public Treap(Collection<T> initialContents) {
        this.addAll(initialContents);
    }

    @Override
    public int size() {
        return 1 + (left == null ? 0 : left.size()) + (right == null ? 0 : right.size());
    }

    @Override
    public boolean isEmpty() {
        return tuple == null && left == null && right == null;
    }

    @Override
    public boolean contains(Object o) {
        Objects.requireNonNull(o);
        return (tuple != null && o.equals(tuple.value))
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
     * Collect the contents of {@link this} and all child {@link Treap}s into the given list.
     *
     * @param list to collect contents into
     */
    private void addContentsToList(List<T> list) {
        if (left != null) {
            left.addContentsToList(list);
        }

        list.add(tuple.value);

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

    public boolean add(T newItem, Integer priority) {
        Objects.requireNonNull(newItem);

        if (tuple == null) {
            tuple = (priority == null) ? new PriorityTuple<>(newItem)
                                       : new PriorityTuple<>(newItem, priority);
            return true;
        }

        if (newItem.equals(tuple.value)) {
            return false;
        }

        if (newItem.compareTo(tuple.value) < 0) {
            // Add to the left side
            if (left == null) {
                left = (priority == null) ? new Treap<>(newItem)
                                          : new Treap<>(newItem, priority);
                return true;
            } else {
                return left.add(newItem, priority);
            }
        }
        else {
            // Add to the right side
            if (right == null) {
                right = (priority == null) ? new Treap<>(newItem)
                                           : new Treap<>(newItem, priority);
                return true;
            } else {
                return right.add(newItem, priority);
            }
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || tuple == null || tuple.value.getClass() != o.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        T item = (T) o;

        return remove(item);
    }

    private boolean remove(T item) {
        Objects.requireNonNull(item);

        if (tuple != null && item.equals(tuple.value)) {
            tuple = null;
            pullUp();
            return true;
        }

        return (left != null && left.remove(item))
               || (right != null && right.remove(item));
    }

    /**
     * Recursively pull up values from children to fill the gap in this {@link Treap}'s value.
     *
     * @throws IllegalStateException if {@link #tuple} is not {@code null}
     */
    private void pullUp() {
        if (tuple != null) {
            throw new IllegalStateException(
                    String.format("Expected tuple to be null, but found %s.", tuple.toString()));
        }


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
        left = null;
        right = null;
        tuple = null;
    }

    @Override
    public String toString() {
        if (tuple == null) {
            return "Empty Treap";
        }

        return tuple.toString();

        /*
        StringBuilder sb = new StringBuilder();
        toStringAppend(sb, 0, getHeight());
        return sb.toString();
        */
    }

    /*
    private void toStringAppend(StringBuilder sb, int level, int height) {
        for (int i = 0; i < height - level; i++) {
            sb.append("    ");
        }

        sb.append(tuple.toString()).append('\n');

        if (left != null) {
            left.toStringAppend(sb, level + 1, height);
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(' ');

        if (right != null) {
            right.toStringAppend(sb, level + 1, height);
        }
    }

    private int getHeight() {
        if (left == null && right == null) {
            return 1;
        }

        if (left == null) {
            return 1 + right.getHeight();
        }

        if (right == null) {
            return 1 + left.getHeight();
        }

        return 1 + Math.max(left.getHeight(), right.getHeight());
    }
    */

    private class PriorityTuple<V extends Comparable<V>> implements Comparable<PriorityTuple<V>> {

        /**
         * The value held by {@link this}.
         */
        private final V value;

        /**
         * The priority of the node in the tree. Always increase as one moves up in the tree.
         */
        private int priority;

        private PriorityTuple(V value, int priority) {
            this.value = Objects.requireNonNull(value);
            this.priority = priority;
        }

        private PriorityTuple(V value) {
            this(value, random.nextInt(100));
        }

        @Override
        public String toString() {
            return tuple.value + " (" + tuple.priority + ')';
        }

        @Override
        public int compareTo(PriorityTuple<V> o) {
            return value.compareTo(o.value);
        }
    }
}
