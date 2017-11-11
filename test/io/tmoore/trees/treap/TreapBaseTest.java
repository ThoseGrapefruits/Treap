package io.tmoore.trees.treap;

import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class TreapBaseTest {
    static final Random random = new Random();
    static final int RANDOM_DATA_SIZE = 64;
    final Set<Integer> randomData = new HashSet<>(RANDOM_DATA_SIZE);
    final Treap<Integer> treap = new Treap<>();

    @BeforeEach
    void setupEach() {
        while (randomData.size() < RANDOM_DATA_SIZE) {
            randomData.add(random.nextInt());
        }
        treap.addAll(randomData);
    }
}
