package io.tmoore.treap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class TreapTest extends TreapBaseTest {

    @RepeatedTest(10)
    void testAddRemove() {
        List<Integer> randomDataList = new ArrayList<>(randomData);
        int split = randomData.size() / 2;
        List<Integer> toRemove = randomDataList.subList(0, split);
        List<Integer> remaining = randomDataList.subList(split, randomData.size());


        for (Integer n : toRemove) {
            treap.remove(n);
            Assertions.assertTrue(!treap.contains(n), "should not contain removed value");
        }

        for (Integer n : remaining) {
            Assertions.assertTrue(treap.contains(n), "should contain remaining values");
        }
    }

    @Test
    void testRemoveAll() {
        treap.removeAll(randomData);
        Assertions.assertTrue(treap.isEmpty());
    }

    @Test
    void testRemoveRoot() {
        Treap<Integer> t = new Treap<>(Collections.singletonList(0));
        t.remove(0);
        Assertions.assertNull(t.getRoot());
        Assertions.assertTrue(t.isEmpty());
    }

    @Test
    void testToArray() {
        final Integer[] array = new Integer[RANDOM_DATA_SIZE];
        treap.toArray(array);
        final Set<Integer> arraySet = new HashSet<>(Arrays.asList(array));

        Assertions.assertEquals(arraySet, randomData);
    }

    @Test
    void testRootReassignment() {
        Treap<Character> treap = new Treap<>(
                new TreapNode<>('A', 10));
        treap.add('N', 100);
        Assertions.assertEquals('N', treap.getRoot().getValue().charValue());
    }
}
