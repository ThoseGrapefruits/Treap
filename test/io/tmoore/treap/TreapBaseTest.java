package io.tmoore.treap;

import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class TreapBaseTest {
    private static final Random random = new Random();
    static final int RANDOM_DATA_SIZE = 64;
    final Set<Integer> randomData = new HashSet<>(RANDOM_DATA_SIZE);
    final Treap<Integer> treap = new Treap<>();

    @BeforeEach
    void setupEach() {
        Set<Integer> randomDataSet = new HashSet<>(RANDOM_DATA_SIZE);
        while (randomDataSet.size() < RANDOM_DATA_SIZE) {
            randomDataSet.add(random.nextInt(255));
        }
        randomData.addAll(randomDataSet);
        treap.addAll(randomDataSet);
    }
}
