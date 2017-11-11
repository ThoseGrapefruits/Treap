package io.tmoore.treap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

class TreapPerformanceTests {
    private static final Random random = new Random();
    private static final int[] scales = {30_000, 50_000, 70_000};
    private static final Integer[] randomData = new Integer[Arrays.stream(scales).max().getAsInt() + 1];
    private static List<Treap<Integer>> treaps;
    private static int[] randomSelections;

    @BeforeAll
    static void generateRandomData() {
        Set<Integer> result = new HashSet<>(randomData.length);
        while (result.size() < randomData.length) {
            result.add(random.nextInt());
        }
        result.toArray(randomData);

        treaps = Arrays.stream(scales).parallel()
                       .mapToObj(value -> Arrays.copyOfRange(randomData, 0, Math.min(value, randomData.length)))
                       .map(integers -> new Treap<>(Arrays.asList(integers)))
                       .filter(Objects::nonNull)
                       .collect(Collectors.toList());

        randomSelections = Arrays.stream(scales)
                                 .map(size -> randomData[random.nextInt(size)])
                                 .toArray();
    }

    private void testSearchTime(int i) {
        Treap<Integer> treap = treaps.get(i);
        Assertions.assertTrue(treap.contains(randomSelections[i]));
    }

    @RepeatedTest(1000)
    void testSearchTime_50_000() {
        testSearchTime(0);
    }

    @RepeatedTest(1000)
    void testSearchTime_100_000() {
        testSearchTime(1);
    }

    @RepeatedTest(1000)
    void testSearchTime_150_000() {
        testSearchTime(2);
    }

    private void testRemoveAddTime(int i) {
        Treap<Integer> treap = treaps.get(i);
        Assertions.assertTrue(treap.remove(randomSelections[i]));
        Assertions.assertTrue(treap.add(randomSelections[i]));
    }

    @RepeatedTest(1000)
    void testRemoveTime_50_000() {
        testRemoveAddTime(0);
    }

    @RepeatedTest(1000)
    void testRemoveTime_100_000() {
        testRemoveAddTime(1);
    }

    @RepeatedTest(1000)
    void testRemoveTime_150_000() {
        testRemoveAddTime(2);
    }
}
