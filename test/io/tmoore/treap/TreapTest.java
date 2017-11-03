package io.tmoore.treap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TreapTest {

    @Test
    void testRootReassignment() {
        Treap<Character> treap = new Treap<>(
                new TreapNode<>('A', 10));
        treap.add('N', 100);
        Assertions.assertEquals('N', treap.getRoot().getValue().charValue());
    }
}
