package io.tmoore.treap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

class TreapPerformanceTests {
    private static final Random random = new Random();
    private static final int[] scales = {50_000, 100_000, 150_000, 200_000};
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

    @RepeatedTest(4)
    void testSearchTime(RepetitionInfo repetitionInfo) {
        int index = repetitionInfo.getCurrentRepetition() - 1;
        Treap<Integer> treap = treaps.get(index);
        Assertions.assertTrue(treap.contains(randomSelections[index]));
    }
}
