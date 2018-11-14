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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * A {@link TailedList} is an {@link List} implementation where each entry in the list points to the next
 * entry in the list.
 *
 * @param <T> the type of elements contained within the {@link TailedList}
 * @see <a href="https://en.wikipedia.org/wiki/Linked_list">Linked list documentation</a>
 */
public class TailedList<T> implements List<T> {
    private static TailedList<?> EMPTY = new TailedList<>(null, null);

    private final T element;
    private final TailedList<T> tail;

    private TailedList(T element, TailedList<T> tail) {
        this.element = element;
        this.tail = tail;
    }

    @Override
    public TailedList<T> add(T value) {
        return new TailedList<>(value, reverse()).reverse();
    }

    @Override
    public List<T> insert(int index, T value) {
        return null;
    }

    @Override
    public int indexOf(Object lookFor) {
        int index = 0;
        for (TailedList<T> list = this; !list.isEmpty(); list = list.tail, index++) {
            if (Objects.equals(list.element, lookFor)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        validateIndexOutOfBounds(index);

        int loopIdx = 0;
        TailedList<T> computed;
        for (computed = this; loopIdx < index; computed = computed.tail, loopIdx++);
        return computed.element;
    }

    @Override
    public List<T> remove(int index) {
        validateIndexOutOfBounds(index);

        TailedList<T> reversed = (TailedList<T>) TailedList.EMPTY;
        for (TailedList<T> newTail = this; !newTail.isEmpty(); newTail = newTail.tail, index--) {
            if (index != 0) {
                reversed = reversed.add(newTail.element);
            }
        }

        return reversed;
    }

    @Override
    public List<T> filter(Predicate<T> predicate) {
        return null;
    }

    @Override
    public int size() {
        int size = 0;
        for (TailedList<T> list = this; !list.isEmpty(); list = list.tail, size++);
        return size;
    }

    @Override
    public boolean isEmpty() {
        return EMPTY.equals(this);
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl<>(this);
    }

    public static <T> Collector<T, ArrayList<T>, TailedList<T>> collector() {
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };

        return Collector.of(ArrayList::new, ArrayList::add, combiner, TailedList::ofAll);
    }

    private void validateIndexOutOfBounds(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException(format("%s is not in the bounds of 0 and %s", index, size()));
        }
    }

    private TailedList<T> reverse() {
        TailedList<T> corrected = (TailedList<T>) TailedList.EMPTY;
        for (int i = 0; i < size(); i++) {
            corrected = new TailedList<>(get(i), corrected);
        }
        return corrected;
    }

    class IteratorImpl<T> implements Iterator<T> {

        private TailedList<T> position;

        private IteratorImpl(TailedList<T> position) {
            this.position = position;
        }

        @Override
        public boolean hasNext() {
            return !position.isEmpty();
        }

        @Override
        public T next() {
            if (position.isEmpty()) {
                throw new NoSuchElementException("No next element available in the iterator");
            }

            T element = position.element;
            position = position.tail;
            return element;
        }
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    /**
     * Create a new {@link TailedList} containing exactly one entry being the provided element.
     *
     * @param element the element to wrap in a {@link TailedList}
     * @param <T>     the type of the element
     * @return        the {@link TailedList} containing the provided element
     */
    public static <T> TailedList<T> of(T element) {
        return TailedList.of((T[]) new Object[]{element});
    }

    /**
     * Create a new {@link TailedList} containing all the elements provided in the order they were provided.
     *
     * @param elements  the elements to wrap in a {@link TailedList}
     * @param <T>       the type of the elements
     * @return          the {@link TailedList} containing the provided elements
     */
    public static <T> TailedList<T> of(T...elements) {
        TailedList<T> reversed = (TailedList<T>) TailedList.EMPTY;
        for (T element : elements) {
            reversed = new TailedList<>(element, reversed);
        }

        return reversed.reverse();
    }

    /**
     * Create a new {@link TailedList} containing all the elements provided in the order they were provided.
     *
     * @param iterable  the elements to wrap in a {@link TailedList}
     * @param <T>       the type of the elements
     * @return          the {@link TailedList} containing the provided elements
     */
    public static <T> TailedList<T> ofAll(Iterable<T> iterable) {
        TailedList<T> reversed = (TailedList<T>) TailedList.EMPTY;
        for (T element : iterable) {
            reversed = new TailedList<>(element, reversed);
        }

        return reversed.reverse();
    }
}
