package io.tmoore.trees.interfaces;

public interface TreeNode<T> {

    /**
     * Get the left child of {@link this}.
     * @return the left child of {@link this}.
     */
    TreeNode<T> getLeft();

    /***
     * Get the right child of {@link this}.
     * @return the right child of {@link this}.
     */
    TreeNode<T> getRight();
}
