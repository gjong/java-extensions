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
package com.jongsoft.lang.collection.impl;

import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.Iterator;
import com.jongsoft.lang.collection.Tree;
import com.jongsoft.lang.collection.support.Collections;
import com.jongsoft.lang.control.impl.Some;

public class TreeSet<T> extends Some<T> implements Tree<T> {

    private String label;
    private Tree<T> parent;
    private NodeCollection<T> children;

    public TreeSet(String label, T value) {
        super(value);

        this.label = label;
        this.children = new NodeCollectionImpl<>(new Object[0]);
    }

    public TreeSet(String label, T value, Tree<T> parent) {
        super(value);

        this.label = label;
        this.parent = parent;
        this.children = new NodeCollectionImpl<>(new Object[0]);
    }

    public TreeSet(String label, T value, Iterable<Tree<T>> children) {
        super(value);

        this.label = label;
        this.children = new NodeCollectionImpl<>(API.Set(children)
                .iterator()
                .toNativeArray());
    }

    @Override
    public Tree<T> parent() {
        return parent;
    }

    @Override
    public NodeCollection<T> children() {
        return children;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public Tree<T> appendChild(final String label, final T child) {
        final Tree<T> childNode = new TreeSet<>(label, child, this);

        Object[] rawChildren = Iterator.concat(this.children.iterator(), API.<Tree<T>>Iterator(childNode))
                                       .toNativeArray();

        this.children = new NodeCollectionImpl<>(rawChildren);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Tree: {value:")
                .append(get())
                .append(", label: ")
                .append(label);
        builder.append(Collections.textValueOf(", children:", children));
        builder.append("}");
        return builder.toString();
    }

    private class NodeCollectionImpl<T> extends HashSet<Tree<T>> implements NodeCollection<T> {

        public NodeCollectionImpl(final Object[] delegate) {
            super(delegate);
        }

    }
}
