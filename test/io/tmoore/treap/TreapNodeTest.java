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
    private List<Character> randomData = new ArrayList<>(randomDataSize);

    @BeforeEach
    void setup() {
        for (int i = 0; i < randomDataSize; i++) {
            randomData.add((char)(0x41 + random.nextInt(26)));
        }
    }

    @Test
    void testAddContains() {
        Treap<Character> treap = new Treap<>();
        treap.addAll(randomData);

        Assertions.assertTrue(treap.containsAll(randomData));
    }

    @Test
    void testAddRemove() {
        Treap<Character> treap = new Treap<>();
        treap.addAll(randomData);

        List<Character> toRemove = randomData.subList(0, 1);
        List<Character> remaining = randomData.subList(1, randomData.size() - 1);

        for (Character n : toRemove) {
            treap.remove(n);
            Assertions.assertTrue(!treap.contains(n), "should not contain removed value");
        }

        for (Character n : remaining) {
            Assertions.assertTrue(treap.contains(n), "should contain remaining values");
        }
    }

    @Test
    void testToArray() {
        final Treap<Character> treap = new Treap<>();
        treap.addAll(randomData);

        final Character[] array = new Character[randomDataSize];
        treap.toArray(array);

        for (Character i : array) {
            if (i != null) {
                Assertions.assertTrue(randomData.contains(i));
            }
        }
    }
}
