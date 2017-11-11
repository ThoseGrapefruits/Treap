package io.tmoore.trees.interfaces;

public interface Paintable {

    /**
     * Mark {@link this} as painted, until {@link #unPaint()} is called.
     */
    default void paint() {
        setPainted(true);
    }

    /**
     * Unmark {@link this} as painted, until {@link #paint()} is called.
     */
    default void unPaint() {
        setPainted(false);
    }

    /**
     * Set whether or not {@link this} is painted;
     */
    void setPainted(boolean painted);

    /**
     * Whether {@link this} has been painted. Should return {@code false} until {@link #paint()} is
     * called for the first time.
     * @return whether {@link this} has been painted.
     */
    boolean isPainted();
}
