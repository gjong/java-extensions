package com.jongsoft.lang.collection;

import com.jongsoft.lang.Value;

/**
 *
 * @param <T> the entity type contained in the tree
 * @since 0.0.5
 */
public interface Tree<T> extends Value<T> {

    /**
     * The node collection is a set of tree elements each containing exactly one value.
     *
     * @param <T> the entity type contained in the tree
     */
    interface NodeCollection<T> extends Set<Tree<T>> {

    }

    /**
     * Fetch the collection of child tree elements contained within this tree.
     *
     * @return  the child collection
     */
    NodeCollection<T> children();

    /**
     * Fetch the label that belongs to the current tree.
     *
     * @return  the string label of the tree
     */
    String label();

    /**
     * Indicates if the tree is a leaf node.
     * This will be {@code true} when the Tree has not children.
     *
     * @return true if {@link #children()} is empty
     */
    default boolean leaf() {
        return children().isEmpty();
    }

}
