package io.tmoore.treap;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiFunction;

class TreapNode<T extends Comparable<T>> implements Comparable<TreapNode<T>> {
    static final String NULL_NODE_STRING = "----";

    private static final Random random = new Random();

    private TreapNode<T> left;
    private TreapNode<T> right;

    /**
     * Get the left child of this {@link TreapNode}.
     *
     * @return the left child
     */
    TreapNode<T> getLeft() {
        return left;
    }

    /**
     * Get the right child of this {@link TreapNode}.
     *
     * @return the right child
     */
    TreapNode<T> getRight() {
        return right;
    }

    private final T value;

    /**
     * Get the value held by this {@link TreapNode}.
     *
     * @return the held value
     */
    T getValue() {
        return value;
    }

    private int priority;

    /**
     * The heap priority of the node in the tree. Always decreasing with depth.
     *
     * @return the heap priority of the node in the tree.
     */
    int getPriority() {
        return priority;
    }

    private TreapNode(T value, TreapNode<T> right, TreapNode<T> left) {
        this(value, random.nextInt(Integer.MAX_VALUE - 2) + 1, right, left);
    }

    TreapNode(T value, int priority, TreapNode<T> right, TreapNode<T> left) {
        Objects.requireNonNull(value);
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
        return aggregateDepth(Integer::sum);
    }

    /**
     * Collect the contents of this and all child {@link TreapNode}s into the given list.
     *
     * @param list to collect contents into
     */
     void addContentsToList(List<T> list) {
        if (left != null) {
            left.addContentsToList(list);
        }

        list.add(value);

        if (right != null) {
            right.addContentsToList(list);
        }
    }

    boolean add(TreapNode<T> newNode) {
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
        if (value.equals(item)) {
            return true;
        }
        if (value.compareTo(item) > 0)  {
            return left != null && left.contains(item);
        }
        return right != null && right.contains(item);
    }

    boolean remove(T item) {
        Objects.requireNonNull(item);
        if (item.equals(value)) {
            priority = Integer.MIN_VALUE;
            return true;
        }

        return (left != null && left.remove(item))
               || (right != null && right.remove(item));
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
              .append(NULL_NODE_STRING).append(System.lineSeparator());
        }
        else {
            right.toStringRecursive(sb, depth + 1);
        }
        if (left == null) {
            sb.append(String.format("%1$" + ((depth + 1) * 4) + "s", ""))
              .append(NULL_NODE_STRING).append(System.lineSeparator());
        }
        else {
            left.toStringRecursive(sb, depth + 1);
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", value, priority);
    }

    @Override
    public int compareTo(TreapNode<T> o) {
        return Integer.compare(priority, o.priority);
    }

    /**
     * The length of the longest branch of the tree.
     *
     * @return length of the longest branch of the tree;
     */
    int maxDepth() {
        return aggregateDepth(Math::max);
    }

    /**
     * The length of the shortest branch of the tree.
     *
     * @return length of the shortest branch of the tree;
     */
    int minDepth() {
        return aggregateDepth(Math::min);
    }

    /**
     * Tail-recursive reducer of depth using the given {@link BiFunction}.
     * @param consumer the {@link Integer} aggregator
     * @return the processed depth
     */
    private int aggregateDepth(BiFunction<Integer, Integer, Integer> consumer) {
        return 1 + consumer.apply(left == null ? 0 : left.aggregateDepth(consumer),
                              right == null ? 0 : right.aggregateDepth(consumer));
    }

    /**
     * A fast estimate of the depth of the tree, assuming the tree is fairly balanced. Only considers leftmost depth.
     *
     * @return estimated depth of the tree, from the height of the left edge.
     */
    int leftDepth() {
        return 1 + (left == null ? 0 : left.leftDepth());
    }
}

