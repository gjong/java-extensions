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

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.Iterator;
import com.jongsoft.lang.collection.Sequence;
import com.jongsoft.lang.collection.Traversable;
import com.jongsoft.lang.collection.Tree;
import com.jongsoft.lang.collection.support.Collections;
import com.jongsoft.lang.control.Optional;

public class TreeSet<T> implements Tree<T> {

    private T value;
    private String label;
    private Tree<T> parent;
    private NodeCollection<T> children;

    public TreeSet(String label, T value) {
        this.value = value;
        this.label = label;
        this.children = new NodeCollectionImpl<>(new Object[0]);
    }

    public TreeSet(String label, T value, Tree<T> parent) {
        this.value = value;
        this.label = label;
        this.parent = parent;
        this.children = new NodeCollectionImpl<>(new Object[0]);
    }

    public TreeSet(String label, T value, Iterable<Tree<T>> children) {
        this.value = value;
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
    public T get() {
        return value;
    }

    @Override
    public boolean isSingleValued() {
        return children.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return foldLeft(API.<T>List(), Sequence::append)
                .iterator();
    }

    @Override
    public <U> Tree<U> map(Function<T, U> mapper) {
        return API.Tree(label, mapper.apply(value), children.map(t -> t.map(mapper)));
    }

    @Override
    public Tree<T> orElse(final Iterable<? extends T> other) {
        return this;
    }

    @Override
    public Tree<T> orElse(final Supplier<? extends Iterable<? extends T>> supplier) {
        return this;
    }

    @Override
    public Traversable<T> filter(Predicate<T> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Traversable<T> reject(Predicate<T> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> first(Predicate<T> predicate) {
        return foldLeft(API.<T>List(), Sequence::append)
                .first(predicate);
    }

    @Override
    public Optional<T> last(Predicate<T> predicate) {
        return foldLeft(API.<T>List(), Sequence::append)
                .last(predicate);
    }

    @Override
    public <U> U foldLeft(U start, BiFunction<? super U, ? super T, ? extends U> combiner) {
        U x = start;
        x = combiner.apply(x, get());
        for (Tree<T> child : children) {
            x = child.foldLeft(x, combiner);
        }
        return x;
    }

    @Override
    public <U> U foldRight(U start, BiFunction<? super T, ? super U, ? extends U> combiner) {
        return foldLeft(start, (y, x) -> combiner.apply(x, y));
    }

    @Override
    public T reduceLeft(BiFunction<? super T, ? super T, ? extends T> reducer) {
        throw new UnsupportedOperationException();
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
