/*
 * The MIT License
 *
 * Copyright 2016-2019 Jong Soft.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
