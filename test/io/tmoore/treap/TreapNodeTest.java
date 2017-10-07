package io.tmoore.treap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class TreapNodeTest {
    private static final Random random = new Random();
    private static final int randomDataSize = 10;
    private List<Integer> randomData = new ArrayList<>(randomDataSize);

    @BeforeEach
    void setup() {
        for (int i = 0; i < randomDataSize; i++) {
            randomData.add(random.nextInt(100));
        }
    }

    @Test
    void testAddContains() {
        Treap<Integer> treap = new Treap<>();
        treap.addAll(randomData);

        Assertions.assertTrue(treap.containsAll(randomData));
    }

    @Test
    void testAddRemove() {
        Treap<Integer> treap = new Treap<>();
        treap.addAll(randomData);

        List<Integer> toRemove = randomData.subList(0, 1);
        List<Integer> remaining = randomData.subList(1, randomData.size() - 1);

        for (Integer n : toRemove) {
            treap.remove(n);
            Assertions.assertTrue(!treap.contains(n), "should not contain removed value");
        }

        for (Integer n : remaining) {
            Assertions.assertTrue(treap.contains(n), "should contain remaining values");
        }
    }

    @Test
    void testToString() {
        Treap<Character> treap = new Treap<>(
                new TreapNode<>('F', 10,
                                new TreapNode<>('T', 7,
                                                new TreapNode<>('X', 6),
                                                new TreapNode<>('H', 3)),
                                new TreapNode<>('D', 8,
                                                new TreapNode<>('E', 1),
                                                new TreapNode<>('C', 2))));
        treap.add('K', 5);

        System.out.println(treap.toString());
    }

    @Test
    void testToArray() {
        final Treap<Integer> treap = new Treap<>();
        treap.addAll(randomData);

        final Integer[] array = new Integer[randomDataSize];
        treap.toArray(array);

        for (Integer i : array) {
            if (i != null) {
                Assertions.assertTrue(randomData.contains(i));
            }
        }
    }
}
