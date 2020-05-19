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

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.Iterator;
import com.jongsoft.lang.collection.List;
import com.jongsoft.lang.collection.Map;
import com.jongsoft.lang.collection.Pipeline;
import com.jongsoft.lang.collection.Sequence;
import com.jongsoft.lang.collection.Set;
import com.jongsoft.lang.collection.support.AbstractIterator;
import com.jongsoft.lang.collection.support.Collections;
import com.jongsoft.lang.collection.support.PipeCommand;

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
    public Sequence<T> append(T value) {
        return new TailedList<>(value, (TailedList<T>) reverse()).reverse();
    }

    @Override
    public Sequence<T> union(final Iterable<T> iterable) {
        Sequence<T> reversed = reverse();
        for (T value : iterable) {
            reversed = new TailedList<>(value, (TailedList<T>) reversed);
        }

        return reversed.reverse();
    }

    @Override
    public Sequence<T> insert(int index, T value) {
        validateIndexOutOfBounds(index);

        TailedList<T> result = this;
        for (int i = 0; i < index; i++) {
            result = result.tail;
        }

        result = new TailedList<>(value, result);
        for (int i = index - 1; i >= 0; i--) {
            result = new TailedList<>(get(i), result);
        }

        return result;
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
    public Sequence<T> tail() {
        return tail;
    }

    @Override
    public Sequence<T> remove(int index) {
        validateIndexOutOfBounds(index);

        Sequence<T> reversed = empty();
        for (TailedList<T> newTail = this; !newTail.isEmpty(); newTail = newTail.tail, index--) {
            if (index != 0) {
                reversed = reversed.append(newTail.get());
            }
        }

        return reversed;
    }

    @Override
    public Sequence<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "The predicate may not be null");

        Sequence<T> filtered = empty();
        for (T value : reverse()) {
            if (predicate.test(value)) {
                filtered = new TailedList<>(value, (TailedList<T>) filtered);
            }
        }

        return filtered;
    }

    @Override
    public Set<T> distinct() {
        return null;
    }

    @Override
    public <U> Sequence<U> map(final Function<T, U> mapper) {
        Sequence<U> mappedTail = empty();
        for (TailedList<T> processing = this; !processing.isEmpty(); processing = processing.tail) {
            mappedTail = new TailedList<>(mapper.apply(processing.get()), (TailedList<U>) mappedTail);
        }
        return mappedTail.reverse();
    }

    @Override
    public Pipeline<T> pipeline() {
        return new PipeCommand<>(this);
    }

    @Override
    public<K> Map<K, List<T>> groupBy(Function<? super T, ? extends K> keyGenerator) {
        return Collections.groupBy(TailedList::empty, this, keyGenerator);
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
    public java.util.List<T> toJava() {
        java.util.List<T> result = new java.util.ArrayList<>(size());
        forEach(result::add);
        return result;
    }

    private void validateIndexOutOfBounds(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException(format("%s is not in the bounds of 0 and %s", index, size()));
        }
    }

    @Override
    public Sequence<T> reverse() {
        Sequence<T> corrected = empty();
        for (int i = 0; i < size(); i++) {
            corrected = new TailedList<>(get(i), (TailedList<T>) corrected);
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
    public static <T> Sequence<T> empty() {
        return (TailedList<T>) EMPTY;
    }

    /**
     * Create a new {@link TailedList} containing exactly one entry being the provided element.
     *
     * @param element the element to wrap in a {@link TailedList}
     * @param <T>     the type of the element
     * @return        the {@link TailedList} containing the provided element
     */
    public static <T> Sequence<T> of(T element) {
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
    public static <T> Sequence<T> of(T...elements) {
        return ofAll(API.Iterator(elements));
    }

    /**
     * Create a new {@link TailedList} containing all the elements provided in the order they were provided.
     *
     * @param iterable  the elements to wrap in a {@link TailedList}
     * @param <T>       the type of the elements
     * @return          the {@link TailedList} containing the provided elements
     */
    public static <T> Sequence<T> ofAll(Iterable<? extends T> iterable) {
        TailedList<T> reversed = (TailedList<T>) TailedList.EMPTY;
        for (T element : iterable) {
            reversed = new TailedList<>(element, reversed);
        }

        return reversed.reverse();
    }
}
