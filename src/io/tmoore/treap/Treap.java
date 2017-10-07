package io.tmoore.treap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Treap<T extends Comparable<T>> implements Collection<T> {

    private TreapNode<T> root;

    /* CONSTRUCTORS */

    public Treap(TreapNode<T> root) {
        this.root = root;
    }

    public Treap(){
        root = new TreapNode<>();
    }

    public Treap(T value, TreapNode<T> right, TreapNode<T> left) {
        root = new TreapNode<>(value, right, left);
    }

    Treap(T value, int priority, TreapNode<T> right, TreapNode<T> left) {
        root = new TreapNode<>(value, priority, right, left);
    }

    Treap(T value, int priority) {
        root = new TreapNode<T>(value, priority);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return root.removeIf(filter);
    }

    @Override
    public Spliterator<T> spliterator() {
        return root.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return root.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return root.parallelStream();
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
        return root.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return root.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return root.iterator();
    }

    @Override
    public Object[] toArray() {
        return root.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        //noinspection SuspiciousToArrayCall
        return root.toArray(a);
    }

    @Override
    public boolean add(T newItem) {
        return root.add(newItem);
    }

    @Override
    public boolean remove(Object o) {
        return root.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return root.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return root.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return root.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return root.retainAll(c);
    }

    @Override
    public void clear() {
        root.clear();
    }

    boolean add(T newItem, Integer priority) {
        return root.add(newItem, priority);
    }
}
