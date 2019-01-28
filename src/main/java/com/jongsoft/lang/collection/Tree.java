package com.jongsoft.lang.collection;

import java.util.Objects;

import com.jongsoft.lang.Value;
import com.jongsoft.lang.control.Optional;

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
    interface NodeCollection<T> extends Collection<Tree<T>> {

        Tree<T> get(int i);

    }

    /**
     * Get the parent tree node for this tree node.
     *
     * @return  the parent, or {@code null} if no parent exists
     */
    Tree<T> parent();

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
    default boolean isLeaf() {
        return children().isEmpty();
    }

    /**
     * Indicates if the current tree node is the root of the tree.
     * This call yields the same result as validating if {@linkplain #parent()} equals {@code null}.
     *
     * @return  true if this tree node is the root
     */
    default boolean isRoot() {
        return parent() == null;
    }

    Tree<T> appendChild(String label, T child);

    default Optional<Tree<T>> getChild(String label) {
        return children().first(c -> Objects.equals(c.label(), label));
    }

}
