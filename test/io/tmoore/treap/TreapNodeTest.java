package io.tmoore.treap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

class TreapNodeTest {
    private static final Random random = new Random();
    private static final int randomDataSize = 64;
    private final List<Integer> randomData = new ArrayList<>(randomDataSize);
    private final Treap<Integer> treap = new Treap<>();

    @BeforeEach
    void setupEach() {
        Set<Integer> randomDataSet = new HashSet<>(randomDataSize);
        while (randomDataSet.size() < randomDataSize) {
            randomDataSet.add(random.nextInt(255));
        }
        randomData.addAll(randomDataSet);
        treap.addAll(randomDataSet);
    }

    @Test
    void testAddContains() {
        Assertions.assertTrue(treap.containsAll(randomData));
    }

    @RepeatedTest(100)
    void testAddRemove() {
        List<Integer> toRemove = randomData.subList(0, 1);
        List<Integer> remaining = randomData.subList(1, randomData.size());

        System.out.println("ORIGINAL");
        System.out.println(treap);

        for (Integer n : toRemove) {
            System.out.printf("REMOVING %s\n", n);
            treap.remove(n);
            System.out.println("AFTER");
            System.out.println(treap);
            Assertions.assertTrue(!treap.contains(n), "should not contain removed value");
        }

        for (Integer n : remaining) {
            Assertions.assertTrue(treap.contains(n), "should contain remaining values");
        }
    }

    @Test
    void testToArray() {
        final Integer[] array = new Integer[randomDataSize];
        treap.toArray(array);

        for (Integer i : array) {
            if (i != null) {
                Assertions.assertTrue(randomData.contains(i));
            }
        }
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
        final Queue<TreapNode<Integer>> stack = new ArrayDeque<>();
        stack.add(treap.getRoot());
    }
}
