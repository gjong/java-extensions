/*
 * The MIT License
 *
 * Copyright 2016 Jong Soft.
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
import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * The {@link Array} implementation of the {@link Sequence} interface provides access to an immutable array. This means all mutable operators
 * will return a new instance rather then modifying the current one.
 *
 * @param <T>   the element type contained in the array
 */
public class Array<T> implements Sequence<T> {

    private static final Array<?> EMPTY = new Array<>(new Object[0]);

    private final Object[] delegate;

    private Array(Object[] delegate) {
        this.delegate = delegate;
    }

    @Override
    public int size() {
        return delegate.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        validateOutOfBounds(index);
        return (T) delegate[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return Iterator.of((T[]) delegate);
    }

    @Override
    public Array<T> filter(Predicate<T> predicate) {
        return stream()
                .filter(predicate)
                .collect(collector());
    }

    @Override
    @SuppressWarnings("Duplicates")
    public <U> Array<U> map(final Function<T, U> mapper) {
        Objects.requireNonNull(mapper, "The mapper cannot be null for this operation.");

        Object[] mapped = new Object[delegate.length];
        for (int i = 0; i < size(); i++) {
            mapped[i] = mapper.apply(get(i));
        }

        return create(mapped);
    }

    @Override
    public Array<T> addAll(final Iterable<T> values) {
        Object[] toBeAdded = toArray(values);
        Object[] newDelegate = new Object[delegate.length + toBeAdded.length];
        System.arraycopy(delegate, 0, newDelegate, 0, delegate.length);
        System.arraycopy(toBeAdded, 0, newDelegate, delegate.length, toBeAdded.length);
        return create(newDelegate);
    }

    @Override
    public Array<T> insert(int index, T value) {
        Object[] newDelegate = new Object[delegate.length + 1];
        System.arraycopy(delegate, 0, newDelegate, 0, index);
        newDelegate[index] = value;
        System.arraycopy(delegate, index, newDelegate, index + 1, delegate.length - index);
        return create(newDelegate);
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
    public Array<T> remove(int index) {
        validateOutOfBounds(index);
        Object[] newDelegate = new Object[delegate.length - 1];

        System.arraycopy(delegate, 0, newDelegate, 0, index);
        System.arraycopy(delegate, index  + 1, newDelegate, index, delegate.length - index - 1);

        return create(newDelegate);
    }

    @SuppressWarnings("Duplicates")
    public static <T> Collector<T, ArrayList<T>, Array<T>> collector() {
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };

        return Collector.of(ArrayList::new, ArrayList::add, combiner, Array::ofAll);
    }

    private void validateOutOfBounds(int index) {
        if (index >= delegate.length || index < 0) {
            throw new IndexOutOfBoundsException(format("%s is not in the bounds of 0 and %s", index, delegate.length));
        }
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    private static <T> Array<T> create(Object[] array) {
        return array.length == 0
                ? empty()
                : new Array<>(array);
    }

    /**
     * Creates an empty array.
     *
     * @param <T>   the type for the empty array
     * @return      the empty array list
     */
    @SuppressWarnings("unchecked")
    public static <T> Array<T> empty() {
        return (Array<T>) EMPTY;
    }

    /**
     * Creates an {@link Array} containing exactly one element, being the one passed to this call.
     *
     * @param element   the element to add to the new array
     * @param <T>       the type of the element
     * @return          the new array list
     */
    public static <T> Array<T> of(T element) {
        return create(new Object[]{element});
    }

    /**
     * Creates a new {@link Array} with the provided elements as the contents.
     *
     * @param elements  the elements to add to the array
     * @param <T>       the type that the elements represent
     * @return          the new array list
     *
     * @throws NullPointerException in case the passed elements is null
     */
    public static <T> Array<T> of(T...elements) {
        Objects.requireNonNull(elements, "The provided elements cannot be null");
        return create(copyOf(elements, elements.length));
    }

    /**
     * Creates a new {@link Array} with all the elements contained in the {@link Iterable}.
     *
     * @param elements  the elements that should be in the new array
     * @param <T>       the type of the elements
     * @return          the new array
     */
    @SuppressWarnings("unchecked")
    public static <T> Array<T> ofAll(Iterable<? extends T> elements) {
        return elements instanceof Array
                ? (Array<T>) elements
                : create(toArray(elements));
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] toArray(Iterable<T> elements) {
        if (elements instanceof java.util.List) {
            final java.util.List<T> list = (java.util.List<T>) elements;
            return (T[]) list.toArray();
        } else {
            final java.util.Iterator<? extends T> it = elements.iterator();
            final java.util.List<T> list = new java.util.ArrayList<>();
            while (it.hasNext()) {
                list.add(it.next());
            }
            return (T[]) list.toArray();
        }
    }
}
