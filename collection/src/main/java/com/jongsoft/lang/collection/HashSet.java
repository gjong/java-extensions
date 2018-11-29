/*
 * The MIT License
 *
 * Copyright 2016-2018 Jong Soft.
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

import static java.lang.String.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

public class HashSet<T> implements Set<T> {
    private static final HashSet EMPTY = new HashSet(new Object[0]);

    private final Object[] delegate;

    private HashSet(final Object[] delegate) {
        this.delegate = delegate;
    }

    @Override
    public Set<T> add(final T value) {
        if (contains(value)) {
            return this;
        }

        Object[] newDelegate = new Object[delegate.length + 1];
        System.arraycopy(delegate, 0, newDelegate, 0, delegate.length);
        newDelegate[delegate.length] = value;
        return new HashSet<>(newDelegate);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public int firstIndexOf(final Predicate<T> predicate) {
        for (int i = 0; i < size(); i++) {
            if (predicate.test(get(i))) {
                return i;
            }
        }

        return -1;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public HashSet<T> remove(final int index) {
        validateOutOfBounds(index);
        Object[] clone = new Object[delegate.length - 1];

        System.arraycopy(delegate, 0, clone, 0, index);
        System.arraycopy(delegate, index  + 1, clone, index, delegate.length - index - 1);

        return new HashSet<>(clone);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(final int index) {
        validateOutOfBounds(index);
        return (T) delegate[index];
    }

    @Override
    @SuppressWarnings("Duplicates")
    public HashSet<T> tail() {
        if (size() == 0) {
            throw new NoSuchElementException("Cannot call tail on empty collection");
        } else if (size() == 1) {
            return empty();
        }

        Object[] tail = new Object[delegate.length - 1];
        System.arraycopy(delegate, 1, tail, 0, delegate.length - 1);
        return new HashSet<>(tail);
    }

    @Override
    public Set<T> filter(final Predicate<T> predicate) {
        return stream()
                .filter(predicate)
                .collect(collector());
    }

    @Override
    @SuppressWarnings("Duplicates")
    public <U> Set<U> map(final Function<T, U> mapper) {
        Objects.requireNonNull(mapper, "The mapper cannot be null for this operation.");

        Set<U> mappedSet = empty();
        for (int i = 0; i < size(); i++) {
            mappedSet = mappedSet.add(mapper.apply(get(i)));
        }

        return mappedSet;
    }

    @Override
    public int size() {
        return delegate.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return Iterator.of((T[]) delegate);
    }

    @Override
    public boolean contains(final T element) {
        Objects.requireNonNull(element, "A set cannot contain a null value");
        int entityHash = element.hashCode();
        return exists(e -> e.hashCode() == entityHash);
    }

    @Override
    public <U> U foldRight(final U start, final BiFunction<? super T, ? super U, ? extends U> combiner) {
        Objects.requireNonNull(combiner, "combiner is null");
        return foldLeft(start, (x , y) -> combiner.apply(y, x));
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

    private void validateOutOfBounds(int index) {
        if (index >= delegate.length || index < 0) {
            throw new IndexOutOfBoundsException(format("%s is not in the bounds of 0 and %s", index, delegate.length));
        }
    }

    @SuppressWarnings("Duplicates")
    public static <T> Collector<T, ArrayList<T>, HashSet<T>> collector() {
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };

        return Collector.of(ArrayList::new, ArrayList::add, combiner, HashSet::of);
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    /**
     * Create an empty HashSet.
     *
     * @param <T>   the type of the entities
     * @return  the empty hash set
     */
    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> empty() {
        return EMPTY;
    }

    /**
     * Create a new hash set containing all unique elements.
     *
     * @param elements  the elements for the new set
     * @param <T>       the type of the elements
     * @return  the created set
     */
    public static <T> HashSet<T> of(T...elements) {
        Set<T> newSet = empty();

        for (int i = 0; i < elements.length; i++) {
            newSet = newSet.add(elements[i]);
        }

        return (HashSet<T>) newSet;
    }

    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> of(Iterable<? extends T> iterable) {
        if (iterable instanceof HashSet) {
            return (HashSet<T>) iterable;
        }

        return new HashSet<>(Iterator.of(iterable).toNativeArray());
    }
}
