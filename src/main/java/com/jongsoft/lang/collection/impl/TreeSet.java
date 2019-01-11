package com.jongsoft.lang.collection.impl;

import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.Tree;
import com.jongsoft.lang.control.impl.Some;

public class TreeSet<T> extends Some<T> implements Tree<T> {

    private String label;
    private NodeCollection<T> children;

    public TreeSet(String label, T value) {
        super(value);

        this.label = label;
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
    public NodeCollection<T> children() {
        return children;
    }

    @Override
    public String label() {
        return label;
    }

    private class NodeCollectionImpl<T> extends HashSet<Tree<T>> implements NodeCollection<T> {

        public NodeCollectionImpl(final Object[] delegate) {
            super(delegate);
        }

    }
}
