package io.tmoore.treap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

class TreapNode<T extends Comparable<T>> implements Iterable<T> {

    private static final Random random = new Random();

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

    int getPriority() {
        return priority;
    }

    TreapNode() {
        this(null, null, null);
    }

    private TreapNode(T value, TreapNode<T> right, TreapNode<T> left) {
        this(value, random.nextInt(), right, left);
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
                return rotateLeft().balance();
            }
            if (right.priority == Integer.MIN_VALUE && right.left == null && right.right == null) {
                right = null;
            }
        }

        if (left != null) {
            left = left.balance();
            if (left.priority > priority) {
                return rotateRight().balance();
            }
            if (left.priority == Integer.MIN_VALUE && left.left == null && left.right == null) {
                left = null;
            }
        }

        return this;
    }

    /**
     *
     * @return the new local root node
     */
    private TreapNode<T> rotateRight() {
        final TreapNode<T> l = left;
        left = left.right;
        l.right = this;
        return l;
    }

    /**
     *
     * @return the new local root node
     */
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
            return left != null && left.contains(item);
        }
        return right != null && right.contains(item);
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

    void toStringRecursive(StringBuilder sb, int depth) {
        if (depth != 0) {
            sb.append(String.format("%1$" + (depth * 4) + "s", ""));
        }
        sb.append(toString());
        sb.append(System.lineSeparator());
        if (right == null && left == null) {
            return;
        }

        if (right == null) {
            sb.append(String.format("%1$" + ((depth + 1) * 4) + "s", ""))
              .append("----").append(System.lineSeparator());
        }
        else {
            right.toStringRecursive(sb, depth + 1);
        }
        if (left == null) {
            sb.append(String.format("%1$" + ((depth + 1) * 4) + "s", ""))
              .append("----").append(System.lineSeparator());
        }
        else {
            left.toStringRecursive(sb, depth + 1);
        }
    }

    @Override
    public String toString() {
        if (value == null) {
            return "Empty Treap";
        }

        return String.format("%s (%d)", value, priority);
    }

    public int depth() {
        if (value == null || (left == null && right == null)) {
            return 0;
        }
        return 1 + Math.max(
                left == null ? 0 : left.depth(),
                right == null ? 0 : right.depth());
    }
}

