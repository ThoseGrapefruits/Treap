package io.tmoore.treap;

import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Treap<T extends Comparable<T>> implements Collection<T> {

    private TreapNode<T> root = null;

    // CONSTRUCTORS

    @SuppressWarnings("WeakerAccess")
    public Treap() {
    }

    @SuppressWarnings("WeakerAccess")
    public Treap(Collection<T> fromCollection) {
        addAll(fromCollection);
    }

    Treap(TreapNode<T> root) {
        this.root = root;
    }

    TreapNode<T> getRoot() {
        return root;
    }

    @Override
    public int size() {
        return root == null ? 0 : root.size();
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null || root == null || root.getValue() == null
            || root.getValue().getClass() != o.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked") T item = (T)o;

        return root.contains(item);
    }

    @Override
    public Iterator<T> iterator() {
        return root == null ? Iterators.empty() : new TreapIterator();
    }

    @Override
    public Object[] toArray() {
        return root == null ? new Object[0] : root.toList().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        Objects.requireNonNull(a);
        //noinspection SuspiciousToArrayCall
        return root == null ? a : root.toList().toArray(a);
    }

    boolean add(T newItem, int priority) {
        Objects.requireNonNull(newItem);
        return add(new TreapNode<>(newItem, priority), true);
    }

    private boolean add(TreapNode<T> newNode, boolean balance) {
        if (root == null) {
            root = newNode;
        }
        if (root.add(newNode) && balance) {
            TreapNode<T> newRoot = root.balance();
            if (newRoot != root) {
                root = newRoot;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean add(T newItem) {
        return add(newItem, true);
    }

    private boolean add(T newItem, boolean balance) {
        Objects.requireNonNull(newItem);
        return add(new TreapNode<>(newItem), true);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || root == null || root.getValue() == null
            || root.getValue().getClass() != o.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        T item = (T) o;

        if (root.remove(item)) {
            TreapNode<T> newRoot = root.balance();
            if (newRoot != root) {
                root = newRoot;
            }
            if (root.getPriority() == Integer.MIN_VALUE) {
                root = null;
            }
            return true;
        }
        return false;
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
            changed |= add(item, false);
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
        root = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(System.lineSeparator());
        if (root == null) {
            sb.append(TreapNode.NULL_NODE_STRING);
        }
        else {
            root.toStringRecursive(sb, 0);
        }
        return sb.toString();
    }


    private class TreapIterator implements Iterator<T> {
        Deque<TreapNode<T>> stack = new ArrayDeque<>(size());
        T lastReturned = null;

        private TreapIterator() {
            TreapNode<T> root = Treap.this.getRoot();
            if (root != null) {
                stack.push(root);
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if (stack.isEmpty()) {
                throw new NoSuchElementException("The Iterator is empty.");
            }

            TreapNode<T> next = stack.pop();

            if (next == null) {
                throw new NoSuchElementException("The Iterator is empty.");
            }

            if (next.getRight() != null) {
                stack.push(next.getRight());
            }
            if (next.getLeft() != null) {
                stack.push(next.getLeft());
            }
            lastReturned = next.getValue();
            return next.getValue();
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("remove called twice or before next was ever called");
            }

            Treap.this.remove(lastReturned);
            lastReturned = null;
        }
    }
}
