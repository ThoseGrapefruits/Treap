package io.tmoore.treap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

class TreapNodeTest {
    private static final Random random = new Random();
    private static final int randomDataSize = 64;
    private final Set<Integer> randomData = new HashSet<>(randomDataSize);
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
        treap.add('K', 5);

        System.out.println(treap);
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

    @RepeatedTest(10)
    void testAddRemove() {
        List<Integer> randomDataList = new ArrayList<>(randomData);
        List<Integer> toRemove = randomDataList.subList(0, 1);
        List<Integer> remaining = randomDataList.subList(1, randomData.size());


        for (Integer n : toRemove) {
            treap.remove(n);
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
        final Set<Integer> arraySet = new HashSet<>(Arrays.asList(array));

        Assertions.assertEquals(arraySet, randomData);
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
