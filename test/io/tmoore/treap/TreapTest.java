package io.tmoore.treap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TreapTest extends TreapBaseTest {

    // CORE COLLECTION METHOD TESTS

    @Test
    void testRemove() {
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
        Assertions.assertNull(treap.getRoot());
        Assertions.assertTrue(treap.isEmpty());
    }

    @Test
    void testRemoveRootSingletonTreap() {
        Treap<Integer> t = new Treap<>(Collections.singletonList(0));
        t.remove(0);
        Assertions.assertNull(t.getRoot());
        Assertions.assertTrue(t.isEmpty());
    }

    @Test
    void testRemoveRoot() {
        Integer rootValue = treap.getRoot().getValue();
        treap.remove(rootValue);
        Assertions.assertFalse(treap.contains(rootValue));
    }

    @Test
    void testRootReassignment() {
        Treap<Character> treap = new Treap<>(
                new TreapNode<>('A', 10));
        treap.add('N', 100);
        Assertions.assertEquals('N', treap.getRoot().getValue().charValue());
    }

    @Test
    void testToArray() {
        final Integer[] array = new Integer[RANDOM_DATA_SIZE];
        treap.toArray(array);
        final Set<Integer> arraySet = new HashSet<>(Arrays.asList(array));

        Assertions.assertEquals(arraySet, randomData);
    }

    @Test
    void testToArrayGeneric() {
        final Set<Object> arraySet = new HashSet<>(Arrays.asList(treap.toArray()));
        Assertions.assertEquals(arraySet, randomData);
    }

    @Test
    void testToStringEmpty() {
        Treap<Character> treap = new Treap<>();
        String s = treap.toString();
        String[] split = s.split(System.lineSeparator());
        Assertions.assertEquals(2, split.length);
        Assertions.assertTrue(split[0].contains(treap.getClass().getName()));
        Assertions.assertEquals(TreapNode.NULL_NODE_STRING, split[1]);
    }

    @Test
    void testToStringSingle() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        Treap<Character> treap = new Treap<>(new TreapNode<>('A', 10));
        String s = treap.toString();
        String[] split = s.split(System.lineSeparator());
        Assertions.assertEquals(2, split.length);
        Assertions.assertTrue(split[0].contains(treap.getClass().getName()));
        Assertions.assertNotEquals(-1, split[1].indexOf(treap.getRoot().getValue()));
    }

    @Test
    void testToString() {
        String s = treap.toString();
        String[] split = s.split(System.lineSeparator());
        Assertions.assertTrue(split[0].contains(treap.getClass().getName()));
        int i = 1;
        TreapNode<Integer> current = treap.getRoot();
        while (current.getRight() != null) {
            current = current.getRight();
            i++;
        }
        Assertions.assertTrue(
                split[i + 1].contains(TreapNode.NULL_NODE_STRING)
                || current.getLeft() == null,
                "Either there is a null string for the right node or both children are null");
    }

    @Test
    void testRetainAll() {
        List<Integer> randomDataList = new ArrayList<>(randomData);
        int split = randomDataList.size() / 2;
        List<Integer> toRetain = randomDataList.subList(0, split);
        List<Integer> removed = randomDataList.subList(split, randomDataList.size());
        treap.retainAll(toRetain);

        Assertions.assertTrue(treap.containsAll(toRetain));

        for (Integer i : removed) {
            Assertions.assertFalse(treap.contains(i));
        }
    }

    @Test
    void testContainsEmptyTreap() {
        treap.clear();
        Assertions.assertFalse(treap.contains(0));
    }

    @Test
    void testContainsAll() {
        Assumptions.assumeTrue(treap.containsAll(randomData));
        while (!randomData.add(random.nextInt())) {
            // Make sure we add a single value, in case we hit repeated value(s).
        }
        Assertions.assertFalse(treap.containsAll(randomData));
    }

    @Test
    void testClear() {
        treap.clear();
        Assertions.assertTrue(treap.isEmpty());
    }

    @Test
    void testSize() {
        Assertions.assertEquals(RANDOM_DATA_SIZE, treap.size());
    }

    @Test
    void testSizeEmptyTreap() {
        Assertions.assertEquals(0, new Treap<>().size());
    }


    // ITERATOR TESTS

    @Test
    void testRemoveObjectFromIterator() {
        for (Integer i : treap) {
            randomData.remove(i);
        }
        Assertions.assertTrue(randomData.isEmpty());
    }

    @Test
    void testIteratorRemove() {
        Iterator<Integer> iterator = treap.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        Assertions.assertTrue(treap.isEmpty());
    }

    @Test
    void testIteratorEarlyRemove() {
        Iterator<Integer> iterator = treap.iterator();
        Assertions.assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void testIteratorNextOnEmptyTreap() {
        Iterator<Integer> iterator = new Treap<Integer>().iterator();
        Assertions.assertThrows(NoSuchElementException.class, iterator::next);
    }


    // SPLITERATOR

    @Test
    void testRootSpliterator() {
        Spliterator<Integer> spliterator = treap.spliterator();
        Assumptions.assumeTrue(spliterator.trySplit().tryAdvance(
                integer -> Assertions.assertTrue(
                        integer.equals(treap.getRoot().getRight().getValue())
                        || integer.equals(treap.getRoot().getLeft().getValue()))));
    }

    @Test
    void testEmptySpliterator() {
        treap.clear();
        Assertions.assertNull(treap.spliterator().trySplit());
    }

    /**
     * This test means nothing on a single-core machine, but otherwise, it will verify that the tree
     * is in fact being streamed in parallel. It verifies that the runtime makes sense for how many
     * cores are simultaneously streaming the treap. This assumes that all cores are being used by
     * {@link Treap#parallelStream()}, which seems to be the standard behaviour.
     */
    @Test
    void testParallelStreamParallelism() {
        Assumptions.assumeTrue(treap.parallelStream().isParallel());
        final int waitTime = 100;

        Assertions.assertTimeout(
                Duration.ofMillis(waitTime * treap.size() / Runtime.getRuntime().availableProcessors()),
                () -> Assertions.assertFalse(
                        Double.isNaN(treap.parallelStream()
                                          .mapToInt(Integer::intValue)
                                          .peek(operand -> {
                                              try {
                                                  Thread.sleep(waitTime);
                                              }
                                              catch (InterruptedException ignored) {
                                              }
                                          })
                                          .average()
                                          .orElse(Double.NaN))),
                "Parallel stream should splitting and operate on all processors");
    }
}
