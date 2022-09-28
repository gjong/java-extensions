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

import com.jongsoft.lang.collection.*;
import com.jongsoft.lang.collection.support.Collections;
import com.jongsoft.lang.collection.support.PipeCommand;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.format;

/**
 * The {@link Array} implementation of the {@link Sequence} interface provides access to an immutable collection of elements.
 * This means all mutable operators will return a new instance rather then modifying the current one.
 *
 * @param <T>   the element type contained in the array
 * @since 0.0.2
 */
public class Array<T> implements Sequence<T> {

    private final Object[] delegate;
    private final int length;

    public Array(Object[] delegate) {
        this.delegate = delegate;
        this.length = delegate.length;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        validateOutOfBounds(index);
        return (T) delegate[index];
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Sequence<T> tail() {
        if (size() == 0) {
            throw new NoSuchElementException("Cannot call tail on empty collection");
        } else if (size() == 1) {
            return com.jongsoft.lang.Collections.List();
        }

        Object[] tail = new Object[delegate.length - 1];
        System.arraycopy(delegate, 1, tail, 0, delegate.length - 1);
        return new Array<>(tail);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return com.jongsoft.lang.Collections.Iterator((T[]) delegate);
    }

    @Override
    public Sequence<T> filter(Predicate<T> predicate) {
        return Collections.filter(com.jongsoft.lang.Collections.List(), this, predicate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> distinct() {
        return com.jongsoft.lang.Collections.Set((T[]) delegate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> distinctBy(Comparator<T> comparator) {
        return com.jongsoft.lang.Collections.Set(comparator, (T[]) delegate);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public <U> Sequence<U> map(final Function<T, U> mapper) {
        Objects.requireNonNull(mapper, "The mapper cannot be null for this operation.");

        Object[] mapped = new Object[delegate.length];
        for (int i = 0; i < delegate.length; i++) {
            mapped[i] = mapper.apply(get(i));
        }

        return new Array<>(mapped);
    }

    @Override
    public Pipeline<T> pipeline() {
        return new PipeCommand<>(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K> Map<K, ? extends Sequence<T>> groupBy(final Function<? super T, ? extends K> keyGenerator) {
        return (Map<K, ? extends Sequence<T>>) Collections.groupBy(com.jongsoft.lang.Collections::List, this, keyGenerator);
    }

    @Override
    public Sequence<T> union(final Iterable<T> iterable) {
        Object[] toBeAdded = com.jongsoft.lang.Collections.Iterator(iterable).toNativeArray();
        Object[] newDelegate = new Object[length + toBeAdded.length];
        System.arraycopy(delegate, 0, newDelegate, 0, length);
        System.arraycopy(toBeAdded, 0, newDelegate, length, toBeAdded.length);
        return new Array<>(newDelegate);
    }

    @Override
    public Sequence<T> insert(int index, T value) {
        Object[] newDelegate = new Object[length + 1];
        System.arraycopy(delegate, 0, newDelegate, 0, index);
        newDelegate[index] = value;
        System.arraycopy(delegate, index, newDelegate, index + 1, length - index);
        return new Array<>(newDelegate);
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
    @SuppressWarnings("Duplicates")
    public Sequence<T> remove(int index) {
        validateOutOfBounds(index);
        Object[] newDelegate = new Object[length - 1];

        System.arraycopy(delegate, 0, newDelegate, 0, index);
        System.arraycopy(delegate, index  + 1, newDelegate, index, length - index - 1);

        return new Array<>(newDelegate);
    }

    @Override
    public Sequence<T> reverse() {
        Object[] reversed = new Object[length];
        for (int i = 0; i < length; i++) {
            reversed[(length - 1) - i] = delegate[i];
        }

        return new Array<>(reversed);
    }

    @Override
    public Sequence<T> replace(int index, T replacement) {
        validateOutOfBounds(index);

        Object[] newDelegate = new Object[length];
        System.arraycopy(delegate, 0, newDelegate, 0, length);
        newDelegate[index] = replacement;
        return new Array<>(newDelegate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Sequence<T> replaceIf(Predicate<T> predicate, T replacement) {
        Objects.requireNonNull(predicate, "The predicate cannot be null for this operation.");

        Object[] newDelegate = new Object[length];
        System.arraycopy(delegate, 0, newDelegate, 0, length);
        for (int index = 0; index < newDelegate.length; index++) {
            if (predicate.test((T) newDelegate[index])) {
                newDelegate[index] = replacement;
            }
        }

        return new Array<>(newDelegate);
    }

    @Override
    public Sequence<T> sorted() {
        Object[] clone = Arrays.copyOf(delegate, length);
        Arrays.sort(clone);
        return new Array<>(clone);
    }

    @Override
    @SuppressWarnings("unchecked")
    public java.util.List<T> toJava() {
        java.util.List<T> result = new java.util.ArrayList<>(length);
        for (Object o : delegate) {
            result.add((T) o);
        }
        return result;
    }

    @Override
    public String toString() {
        return Collections.textValueOf("Sequence", this);
    }

    private void validateOutOfBounds(int index) {
        if (index >= length || index < 0) {
            throw new IndexOutOfBoundsException(format("%s is not in the bounds of 0 and %s", index, length));
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean equals(final Object obj) {
        if (obj instanceof Sequence) {
            Sequence casted = (Sequence) obj;

            return casted.size() == size()
                    && casted.containsAll(this)
                    && containsAll(casted);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return foldLeft(19, (left, right) -> left + (right != null ? right.hashCode() : 0));
    }
}
