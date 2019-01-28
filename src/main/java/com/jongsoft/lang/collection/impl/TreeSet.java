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

        Object[] rawChildren = Iterator.concat(this.children.iterator(), Iterator.<Tree<T>>of(childNode))
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
