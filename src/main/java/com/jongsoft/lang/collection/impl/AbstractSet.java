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

import static java.lang.String.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.*;
import com.jongsoft.lang.collection.support.Collections;
import com.jongsoft.lang.collection.support.PipeCommand;
import com.jongsoft.lang.collection.tuple.Pair;

abstract class AbstractSet<T> implements Set<T> {

    private final Object[] delegate;

    AbstractSet(Object[] delegate) {
        this.delegate = delegate;
    }

    @Override
    public Set<T> append(final T value) {
        if (contains(value)) {
            return this;
        }

        Object[] newDelegate = new Object[delegate.length + 1];
        System.arraycopy(delegate, 0, newDelegate, 0, delegate.length);
        newDelegate[delegate.length] = value;
        return this.wrapperSupplier().apply(newDelegate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> distinctBy(Comparator<T> comparator) {
        return com.jongsoft.lang.Collections.Set(comparator, (T[]) delegate);
    }

    @Override
    public List<T> sorted() {
        Object[] clone = Arrays.copyOf(delegate, delegate.length);
        Arrays.sort(clone);
        return new Array<>(clone);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Set<T> remove(final int index) {
        validateOutOfBounds(index);
        Object[] clone = new Object[delegate.length - 1];

        System.arraycopy(delegate, 0, clone, 0, index);
        System.arraycopy(delegate, index  + 1, clone, index, delegate.length - index - 1);

        return this.wrapperSupplier().apply(clone);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public int firstIndexWhere(final Predicate<T> predicate) {
        for (int i = 0; i < size(); i++) {
            if (predicate.test(get(i))) {
                return i;
            }
        }

        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(final int index) {
        validateOutOfBounds(index);
        return (T) delegate[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K> Map<K, ? extends Set<T>> groupBy(final Function<? super T, ? extends K> keyGenerator) {
        final Supplier<Set<T>> setSupplier = this.emptySupplier();
        return (Map<K, ? extends Set<T>>) Collections.groupBy(setSupplier::get, this, keyGenerator);
    }

    @Override
    public Pair<? extends Set<T>, ? extends Collection<T>> split(Predicate<T> predicate) {
        Map<Boolean, ? extends Set<T>> split = groupBy(predicate::test);
        return API.Tuple(split.get(true), split.get(false));
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Set<T> tail() {
        if (size() == 0) {
            throw new NoSuchElementException("Cannot call tail on empty collection");
        } else if (size() == 1) {
            return this.<T>emptySupplier().get();
        }

        Object[] tail = new Object[delegate.length - 1];
        System.arraycopy(delegate, 1, tail, 0, delegate.length - 1);
        return this.wrapperSupplier().apply(tail);
    }

    @Override
    public Set<T> filter(final Predicate<T> predicate) {
        return Collections.filter(this.<T>emptySupplier().get(), this, predicate);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public <U> Set<U> map(final Function<T, U> mapper) {
        Objects.requireNonNull(mapper, "The mapper cannot be null for this operation.");

        Set<U> mappedSet = this.<U>emptySupplier().get();
        for (int i = 0; i < size(); i++) {
            mappedSet = mappedSet.append(mapper.apply(get(i)));
        }

        return mappedSet;
    }

    @Override
    public Set<T> orElse(final Iterable<? extends T> other) {
        return isEmpty() ?
                this.wrapperSupplier().apply(com.jongsoft.lang.Collections.Iterator(other).toNativeArray())
                : this;
    }

    @Override
    public Set<T> orElse(final Supplier<? extends Iterable<? extends T>> supplier) {
        return isEmpty() ?
                this.wrapperSupplier().apply(com.jongsoft.lang.Collections.Iterator(supplier.get()).toNativeArray())
                : this;
    }

    @Override
    public Pipeline<T> pipeline() {
        return new PipeCommand<>(this);
    }

    @Override
    public int size() {
        return delegate.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return com.jongsoft.lang.Collections.Iterator((T[]) delegate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public java.util.Set<T> toJava() {
        java.util.Set<T> result = new java.util.HashSet<>(delegate.length);
        for (Object o : delegate) {
            result.add((T) o);
        }
        return result;
    }

    @Override
    public Set<T> union(final Iterable<T> iterable) {
        return Collections.<T, Set<T>>filter(this, iterable, Predicate.not(this::contains));
    }

    @Override
    @SafeVarargs
    public final Set<T> intersect(final Iterable<T>...iterable) {
        if (iterable.length == 0) {
            return this.<T>emptySupplier().get();
        }

        Predicate<T> operation = com.jongsoft.lang.Collections.Set(iterable)
                .map(com.jongsoft.lang.Collections::Set)
                .foldLeft(x -> true, (x, xs) -> x.and(xs::contains));

        return Collections.filter(this.<T>emptySupplier().get(), this, operation);
    }

    @Override
    @SafeVarargs
    public final Set<T> complement(final Iterable<T>...iterables) {
        if (iterables.length == 0) {
            return this;
        }

        Predicate<T> operation = com.jongsoft.lang.Collections.Set(iterables)
                .map(com.jongsoft.lang.Collections::Set)
                .foldLeft(x -> true, (x, xs) -> x.and(Predicate.not(xs::contains)));

        return Collections.filter(this.<T>emptySupplier().get(), this, operation);
    }

    @Override
    public String toString() {
        return Collections.textValueOf("Set", this);
    }

    private void validateOutOfBounds(int index) {
        if (index >= delegate.length || index < 0) {
            throw new IndexOutOfBoundsException(format("%s is not in the bounds of 0 and %s", index, delegate.length));
        }
    }

    protected abstract <X> Supplier<Set<X>> emptySupplier();
    protected abstract Function<Object[], Set<T>> wrapperSupplier();

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object obj) {
        if (obj instanceof Set) {
            Set casted = (Set) obj;

            return casted.size() == size()
                    && casted.intersect(this).size() == size();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return foldLeft(21, (left, right) -> left + (right != null ? right.hashCode() : 0));
    }
}
