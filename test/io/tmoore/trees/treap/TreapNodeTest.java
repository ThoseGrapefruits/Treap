package io.tmoore.trees.treap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

class TreapNodeTest extends TreapBaseTest {
    @Test
    void testAddContains() {
        Assertions.assertTrue(treap.containsAll(randomData));
    }

    @Test
    void testSpecificInsertion() {
        Treap<Character> treap = new Treap<>(
                new TreapNode<>('F', 10,
                                new TreapNode<>('T', 7,
                                                new TreapNode<>('X', 6),
                                                new TreapNode<>('H', 3)),
                                new TreapNode<>('D', 8,
                                                new TreapNode<>('E', 1),
                                                new TreapNode<>('C', 2))));

        Assertions.assertTrue(treap.add('K', 5));

        TreapNode<Character> current = treap.getRoot();
        Assertions.assertNotNull(current);

        current = current.getRight();
        Assertions.assertNotNull(current);

        current = current.getLeft();
        Assertions.assertNotNull(current);

        Character value = current.getValue();
        Assertions.assertNotNull(value);

        Assertions.assertEquals('K', value.charValue());
    }

    @Test
    void testDepthSingleton() {
        TreapNode<Integer> root = new TreapNode<>(0);
        Assertions.assertEquals(1, root.leftDepth());
        Assertions.assertEquals(1, root.maxDepth());
        Assertions.assertEquals(1, root.minDepth());
    }

    @Test
    void testDepthDuo() {
        TreapNode<Integer> root = new TreapNode<>(0, 0, null, new TreapNode<>(0));
        Assertions.assertEquals(2, root.leftDepth());
        Assertions.assertEquals(2, root.maxDepth());
        Assertions.assertEquals(1, root.minDepth());
    }

    @Test
    void testDepth() {
        TreapNode<Integer> root = treap.getRoot();
        Assertions.assertTrue(Math.pow(2, root.maxDepth()) >= root.size());
        Assertions.assertTrue(Math.pow(2, root.minDepth()) <= root.size());
    }


    @Test
    void testHeapProperty() {
        final Queue<TreapNode<Integer>> stack = new ArrayDeque<>();
        stack.add(treap.getRoot());

        while (!stack.isEmpty()) {
            TreapNode<Integer> current = stack.remove();
            if (current.getLeft() != null) {
                Assertions.assertTrue(current.getPriority() > current.getLeft().getPriority());
                stack.add(current.getLeft());
            }
            if (current.getRight() != null) {
                Assertions.assertTrue(current.getPriority() > current.getRight().getPriority());
                stack.add(current.getRight());
            }
        }
    }

    @Test
    void testBTreeProperty() {
        final Deque<TreapNode<Integer>> stack = new ArrayDeque<>();
        stack.add(treap.getRoot());

        while (!stack.isEmpty()) {
            TreapNode<Integer> current = stack.pop();
            if (current.getLeft() != null) {
                Assertions.assertTrue(current.getValue() > current.getLeft().getValue());
                stack.add(current.getLeft());
            }
            if (current.getRight() != null) {
                Assertions.assertTrue(current.getValue() < current.getRight().getValue());
                stack.add(current.getRight());
            }
            if (current.getLeft() != null && current.getRight() != null) {
                Assertions.assertTrue(current.getLeft().getValue() < current.getRight().getValue());
            }
        }
    }
}
