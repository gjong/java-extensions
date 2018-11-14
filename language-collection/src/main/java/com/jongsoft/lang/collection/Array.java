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

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static java.lang.String.format;
import static java.util.Arrays.copyOf;

public class Array<T> implements List<T> {

    private static final Array<?> EMPTY = new Array<>(new Object[0]);

    private final T[] delegate;

    private Array(T[] delegate) {
        this.delegate = delegate;
    }

    @Override
    public int size() {
        return delegate.length;
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        validateOutOfBounds(index);
        return delegate[index];
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(Arrays.asList(delegate)).iterator();
    }

    @Override
    public List<T> filter(Predicate<T> predicate) {
        return stream()
                .filter(predicate)
                .collect(collector());
    }

    @Override
    public List<T> insert(int index, T value) {
        T[] newDelegate = (T[]) new Object[delegate.length + 1];
        System.arraycopy(delegate, 0, newDelegate, 0, index);
        newDelegate[index] = value;
        System.arraycopy(delegate, index, newDelegate, index + 1, delegate.length - index);
        return create(newDelegate);
    }

    @Override
    public int indexOf(Object lookFor) {
        for (int i = 0; i < size(); i++) {
            if (Objects.equals(delegate[i], lookFor)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public List<T> remove(int index) {
        validateOutOfBounds(index);
        T[] newDelegate = (T[]) new Object[delegate.length - 1];

        System.arraycopy(delegate, 0, newDelegate, 0, index);
        System.arraycopy(delegate, index  + 1, newDelegate, index, delegate.length - index - 1);

        return create(newDelegate);
    }

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

    private static <T> Array<T> create(T[] array) {
        return array.length == 0
                ? (Array<T>) EMPTY
                : new Array<>(array);
    }

    public static <T> Array<T> empty() {
        return (Array<T>) EMPTY;
    }

    public static <T> Array<T> of(T element) {
        return create((T[]) new Object[]{element});
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

    public static <T> Array<T> ofAll(Iterable<? extends T> elements) {
        return elements instanceof Array
                ? (Array<T>) elements
                : create(toArray(elements));
    }

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
