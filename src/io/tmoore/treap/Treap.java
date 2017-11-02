package io.tmoore.treap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Treap<T extends Comparable<T>> implements Collection<T> {

    private TreapNode<T> root;

    // CONSTRUCTORS

    @SuppressWarnings("WeakerAccess")
    public Treap(TreapNode<T> root) {
        this.root = root;
    }

    @SuppressWarnings("WeakerAccess")
    public Treap() {
        root = new TreapNode<>();
    }

    TreapNode<T> getRoot() {
        return root;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        root.forEach(action);
    }

    @Override
    public int size() {
        return root.size();
    }

    @Override
    public boolean isEmpty() {
        return root.getValue() == null && root.getLeft() == null && root.getRight() == null;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null || root.getValue() == null || root.getValue().getClass() != o.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked") T item = (T)o;

        return root.contains(item);
    }

    @Override
    public Iterator<T> iterator() {
        return root.iterator();
    }

    @Override
    public Object[] toArray() {
        return root.toList().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        //noinspection SuspiciousToArrayCall
        return root.toList().toArray(a);

    }

    boolean add(T newItem, int priority) {
        Objects.requireNonNull(newItem);
        return add(new TreapNode<>(newItem, priority));
    }

    private boolean add(TreapNode<T> newNode) {
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
        if (o == null || root.getValue() == null || root.getValue().getClass() != o.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        T item = (T) o;

        if (root.remove(item)) {
            TreapNode<T> newRoot = root.balance();
            if (newRoot != root) {
                root = newRoot;
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
        root.clear();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        List<TreapNode<T>> previousLevel = new ArrayList<>();
        List<TreapNode<T>> currentLevel = new ArrayList<>();
        List<String> collected = new ArrayList<>();
        previousLevel.add(root);

        while (true) {
            for (TreapNode<T> node : previousLevel) {
                sb.append(String.format("%1$-6s", node == null ? " " : node.toString()));
                sb.append(' ');
            }

            collected.add(sb.toString());
            sb.setLength(0);

            for (TreapNode<T> node : previousLevel) {
                if (node == null) {
                    currentLevel.add(null);
                }
                else {
                    currentLevel.add(node.getLeft());
                    currentLevel.add(node.getRight());
                }
            }

            if (currentLevel.stream().allMatch(Objects::isNull)) {
                break;
            }

            previousLevel = currentLevel;
            currentLevel = new ArrayList<>();
        }

        sb.setLength(0);

        for (int i = collected.size() - 1; i >= 0; i--) {
            collected.set(i, sb.toString() + collected.get(i));
            sb.append("       ");
        }

        return collected.stream().collect(Collectors.joining("\n"));
    }
}
