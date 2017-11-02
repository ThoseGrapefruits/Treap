package io.tmoore.treap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TreapTest {

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
    void testRootReassignment() {
        Treap<Character> treap = new Treap<>(
                new TreapNode<>('A', 10));
        treap.add('N', 100);
        Assertions.assertEquals('N', treap.getRoot().getValue().charValue());
    }
}
