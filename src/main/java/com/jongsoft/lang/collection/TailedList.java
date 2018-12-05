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
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

import com.jongsoft.lang.collection.support.AbstractIterator;

/**
 * A {@link TailedList} is an {@link Sequence} implementation where each entry in the list points to the next
 * entry in the list.
 *
 * @param <T> the type of elements contained within the {@link TailedList}
 * @see <a href="https://en.wikipedia.org/wiki/Linked_list">Linked list documentation</a>
 * @since 0.0.1
 */
public class TailedList<T> implements Sequence<T> {
    private static final TailedList<?> EMPTY = new TailedList<>(null, null);

    private final Object element;
    private final TailedList<T> tail;

    private TailedList(T element, TailedList<T> tail) {
        this.element = element;
        this.tail = tail;
    }

    @Override
    public TailedList<T> append(T value) {
        return new TailedList<>(value, reverse()).reverse();
    }

    @Override
    public TailedList<T> union(final Iterable<T> iterable) {
        TailedList<T> reversed = reverse();
        for (T value : iterable) {
            reversed = new TailedList<>(value, reversed);
        }

        return reversed.reverse();
    }

    @Override
    public TailedList<T> insert(int index, T value) {
        validateIndexOutOfBounds(index);

        TailedList<T> tail = this;
        for (int i = 0; i < index; i++) {
            tail = tail.tail;
        }

        tail = new TailedList<>(value, tail);
        for (int i = index - 1; i >= 0; i--) {
            tail = new TailedList<>(get(i), tail);
        }

        return tail;
    }

    @Override
    public int firstIndexWhere(final Predicate<T> predicate) {
        int index = 0;
        for (TailedList<T> list = this; !list.isEmpty(); list = list.tail, index++) {
            if (predicate.test(list.get())) {
                return index;
            }
        }
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        validateIndexOutOfBounds(index);

        int loopIdx = 0;
        TailedList<T> computed;
        for (computed = this; loopIdx < index; computed = computed.tail, loopIdx++);
        return (T) computed.element;
    }

    @Override
    public TailedList<T> tail() {
        return tail;
    }

    @Override
    public TailedList<T> remove(int index) {
        validateIndexOutOfBounds(index);

        TailedList<T> reversed = empty();
        for (TailedList<T> newTail = this; !newTail.isEmpty(); newTail = newTail.tail, index--) {
            if (index != 0) {
                reversed = reversed.append(newTail.get());
            }
        }

        return reversed;
    }

    @Override
    public TailedList<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "The predicate may not be null");

        TailedList<T> filtered = empty();
        for (T value : reverse()) {
            if (predicate.test(value)) {
                filtered = new TailedList<>(value, filtered);
            }
        }

        return filtered;
    }

    @Override
    public <U> TailedList<U> map(final Function<T, U> mapper) {
        TailedList<U> mappedTail = empty();
        for (TailedList<T> processing = this; !processing.isEmpty(); processing = processing.tail) {
            mappedTail = new TailedList<>(mapper.apply(processing.get()), mappedTail);
        }
        return mappedTail.reverse();
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

    @Override
    public List<T> toJava() {
        java.util.List<T> result = new java.util.ArrayList<>(size());
        forEach(result::add);
        return result;
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

    @Override
    public TailedList<T> reverse() {
        TailedList<T> corrected = empty();
        for (int i = 0; i < size(); i++) {
            corrected = new TailedList<>(get(i), corrected);
        }
        return corrected;
    }

    class IteratorImpl<T> extends AbstractIterator<T> {

        private TailedList<T> start;
        private TailedList<T> position;

        private IteratorImpl(TailedList<T> position) {
            this.start = position;
            this.position = position;
        }

        @Override
        public void reset() {
            position = start;
        }

        @Override
        public boolean hasNext() {
            return !position.isEmpty();
        }

        @Override
        public T getNext() {
            T value = position.get();
            position = position.tail;
            return value;
        }

    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    /**
     * Creates a new empty TailedList.
     *
     * @param <T>   the type of the list
     * @return      the created list
     */
    @SuppressWarnings("unchecked")
    public static <T> TailedList<T> empty() {
        return (TailedList<T>) EMPTY;
    }

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
    @SafeVarargs
    public static <T> TailedList<T> of(T...elements) {
        return ofAll(Iterator.of(elements));
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
