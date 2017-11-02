package io.tmoore.treap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

class TreapNode<T extends Comparable<T>> implements Iterable<T> {

    private static final Random random = new Random();
    private static final int priorityLimit = 100;

    private static final TreapNode NO_ROOT_CHANGE = new TreapNode();

    private TreapNode<T> left;
    private TreapNode<T> right;

    TreapNode<T> getLeft() {
        return left;
    }

    TreapNode<T> getRight() {
        return right;
    }

    /**
     * The value held by {@link this}.
     */
    private T value;

    T getValue() {
        return value;
    }

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

    TreapNode(T value) {
        this(value, null, null);
    }

    int size() {
        return 1 + (left == null ? 0 : left.size()) + (right == null ? 0 : right.size());
    }

    @Override
    public Iterator<T> iterator() {
        return toList().iterator();
    }

    List<T> toList() {
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

    boolean add(TreapNode<T> newNode) {
        if (value == null) {  // Only for root
            value = newNode.value;
        }

        if (newNode.value.equals(value)) {
            return false;
        }


        if (newNode.value.compareTo(value) < 0) {
            // Add to the left side
            if (left == null) {
                left = newNode;
                return true;
            } else {
                return left.add(newNode);
            }
        }
        else {
            // Add to the right side
            if (right == null) {
                right = newNode;
                return true;
            } else {
                return right.add(newNode);
            }
        }
    }

    TreapNode<T> balance() {
        // Assume 1 change happens at a time, so both branches can't be out of balance.
        if (left == null && right == null) {
            return this;
        }

        if (right != null) {
            right = right.balance();
            if (right.priority > priority) {
                return rotateLeft();
            }
            return this;
        }

        left = left.balance();
        if (left.priority > priority) {
            return rotateRight();
        }
        return this;
    }

    private TreapNode<T> rotateRight() {
        final TreapNode<T> l = left;
        left = left.right;
        l.right = this;
        return l;
    }

    private TreapNode<T> rotateLeft() {
        final TreapNode<T> r = right;
        right = right.left;
        r.left = this;
        return r;
    }

    boolean contains(T item) {
        Objects.requireNonNull(item);
        if (value == null) {
            return false;
        }
        if (value.equals(item)) {
            return true;
        }
        if (value.compareTo(item) > 0)  {
            return left.contains(item);
        }
        return right.contains(item);
    }

    boolean remove(T item) {
        if (item.equals(value)) {
            priority = Integer.MIN_VALUE;
            return true;
        }

        return (left != null && left.remove(item))
               || (right != null && right.remove(item));
    }

    void clear() {
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
}

