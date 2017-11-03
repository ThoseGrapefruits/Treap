package io.tmoore.treap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
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
        StringBuilder sb = new StringBuilder();
        root.toStringRecursive(sb, 0);
        return sb.toString();
    }

    private String verticalToString() {
        int maxWidth = 0;
        final List<List<TreapNode<T>>> levels = new ArrayList<>();
        levels.add(new ArrayList<>(Collections.singletonList(root)));

        boolean allNull;
        do {
            levels.add(new ArrayList<>());
            final List<TreapNode<T>> previousLevel = levels.get(levels.size() - 2);
            final List<TreapNode<T>> currentLevel = levels.get(levels.size() - 1);
            allNull = true;
            for (TreapNode<T> node : previousLevel) {
                if (node == null) {
                    currentLevel.add(null);
                }
                else {
                    maxWidth = Math.max(maxWidth, node.toString().length());
                    allNull = false;
                    currentLevel.add(node.getLeft());
                    currentLevel.add(node.getRight());
                }
            }
        } while (!allNull);

        final String formatString = "%1$" + maxWidth + "s";
        final String tab = String.format(formatString, "");
        final Deque<String> indent = new ArrayDeque<>(Collections.nCopies(
                levels.size(), ""));
        final StringBuilder spacer = new StringBuilder();

        return levels.stream()
                     .map(treapNodes -> {
                         indent.pop();
                         spacer.append(tab.substring(0, tab.length() / 2 - 2));
                         String result =  indent.stream().collect(Collectors.joining(tab))
                                + treapNodes.stream()
                                            .map(node -> node == null ? "" : node.toString())
                                            .map(s -> String.format(formatString, s))
                                            .collect(Collectors.joining(spacer));
                         return result;
                     })
                     .collect(Collectors.joining(System.lineSeparator()));
    }
}
