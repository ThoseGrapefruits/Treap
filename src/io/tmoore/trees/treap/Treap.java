package io.tmoore.trees.treap;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Spliterator;
import java.util.function.Consumer;

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
        return new TreapIterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return new TreapSpliterator();
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
        return add(new TreapNode<>(newItem, priority));
    }

    private boolean add(TreapNode<T> newNode) {
        if (root == null) {
            root = newNode;
        }
        if (root.add(newNode)) {
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
        Objects.requireNonNull(newItem);
        return add(new TreapNode<>(newItem));
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
        return Objects.requireNonNull(c)
                      .stream()
                      .map(this::add)
                      .reduce(false, Boolean::logicalOr);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return Objects.requireNonNull(c)
                      .stream()
                      .map(this::remove)
                      .reduce(false, Boolean::logicalOr);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return stream()
                .filter(element -> !c.contains(element))
                .map(this::remove)
                .reduce(false, Boolean::logicalOr);
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
            if (Treap.this.root != null) {
                stack.push(Treap.this.root);
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

    /**
     * {@link Spliterator} for parallel BFS traversal of a {@link TreapNode} and its children.
     */
    private class TreapSpliterator implements Spliterator<T> {
        private Queue<TreapNode<T>> queue = new PriorityQueue<>();

        /**
         * {@link TreapSpliterator} runs into trouble when {@link #trySplit()} is called but the
         * {@link #queue} only contains the root node. If it creates a new {@link TreapSpliterator}
         * with one of root's children, but keeps {@link this} as-is, then it will iterate over the
         * chosen child (and all of its children) twice. If it sets {@link this} on, say, the left
         * child, and a new {@link TreapSpliterator} on the right child, then it will never iterate
         * over root's value. This serves as a marker for when {@link this} has split but the parent
         * value still has not been processed.
         */
        private T unprocessedParent = null;

        TreapSpliterator(TreapNode<T> root) {
            if (root != null) {
                queue.add(root);
            }
        }

        TreapSpliterator() {
            this(Treap.this.root);
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (unprocessedParent != null) {
                action.accept(unprocessedParent);
                unprocessedParent = null;
                return true;
            }

            if (queue.isEmpty()) {
                return false;
            }

            TreapNode<T> next = queue.remove();

            if (next.getRight() != null) {
                queue.add(next.getRight());
            }

            if (next.getLeft() != null) {
                queue.add(next.getLeft());
            }

            action.accept(next.getValue());
            return true;
        }

        @Override
        public Spliterator<T> trySplit() {
            if (queue.size() == 0) {
                return null;
            }

            if (queue.size() == 1) {
                TreapNode<T> current = queue.remove();
                unprocessedParent = current.getValue();
                if (current.getRight() != null) {
                    queue.add(current.getRight());
                }

                if (current.getLeft() != null) {
                    queue.add(current.getLeft());
                }
            }

            return new TreapSpliterator(queue.remove());
        }

        @Override
        public long estimateSize() {
            return getExactSizeIfKnown();
        }

        @Override
        public long getExactSizeIfKnown() {
            return (unprocessedParent == null ? 0 : 1) + (queue.isEmpty() ? 0 : queue.peek().size());
        }

        @Override
        public Comparator<? super T> getComparator() {
            return null;
        }

        @Override
        public int characteristics() {
            return DISTINCT | NONNULL | SIZED | SUBSIZED | SORTED;
        }
    }
}
