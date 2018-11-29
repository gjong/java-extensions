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

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Sequence implementations will be an ordered {@link Collection}. These collections allow for adding duplicate entries but all entries
 * will always be returned in the order that they were added.
 *
 * @param <T>   the entity type of the sequence
 */
public interface Sequence<T> extends Collection<T> {

    /**
     * Add an element to the end of the sequence and return the newly created instance.
     *
     * <pre>{@code
     *    // will result in a sequence with 2, 3, 4, 1
     *    Sequence(2, 3, 4).append(1)
     * }</pre>
     *
     * @param value     the value to append to the list
     * @return          the new list with the value appended
     */
    default Sequence<T> append(T value) {
        return insert(size(), value);
    }

    /**
     * Add an element to the begin of the sequence and return the new instance with the element.
     *
     * <pre>{@code
     *    // will result in a sequence with 1, 2, 3, 4
     *    Sequence(2, 3, 4).prepend(1)
     * }</pre>
     *
     * @param value     the value to be added
     * @return          the new list with the value appended
     */
    default Sequence<T> prepend(T value) {
        return insert(0, value);
    }

    /**
     * Add all elements to the end of the current sequence, returning a new sequence.
     *
     * @param values    the elements to be added
     * @return          the new list containing a union between this and the values
     */
    Sequence<T> appendAll(Iterable<T> values);

    /**
     * Add an element to the list at the provided index, shifting all elements after the index one.
     *
     * @param index the index at which to insert the element
     * @param value the element to insert
     * @return      the updated list with the inserted element
     */
    Sequence<T> insert(int index, T value);

    /**
     * Find the index for the provided element, will return <code>-1</code> if the element
     * is not present in the list.
     *
     * @param lookFor   the element to look for
     * @return  the index of the element, or <code>-1</code> if none found
     */
    default int indexOf(Object lookFor) {
        return firstIndexWhere(e -> Objects.equals(lookFor, e));
    }

    /**
     * Search the collections for the first element matching the provided {@link Predicate} and return the index
     * position of that element.
     *
     * @param predicate the predicate to match
     * @return  index of the found element, -1 if none found
     */
    int firstIndexWhere(Predicate<T> predicate);

    /**
     * Removes an element from the list and returns a new instance of the list.
     *
     * @param index     the index of the element to be removed
     * @return          the new instance of the list without the element at the provided index
     * @throws IndexOutOfBoundsException    in case the index is not between the 0 and list size
     */
    Sequence<T> remove(int index);

    /**
     * Removes the first element found matching the provided value. The match is done based upon the
     * {@link java.util.Objects#equals(Object, Object)} call.
     *
     * @param value the element to be removed
     * @return the current list if the element is not present, otherwise a new list instance without the element in it.
     */
    default Sequence<T> remove(T value) {
        int idx = indexOf(value);
        if (idx > -1) {
            return remove(idx);
        }

        return this;
    }

    /**
     * Get the element at the location of <code>index</code>
     *
     * @param index     the index of the element in the list to get
     * @return          the element at the provided index
     * @throws IndexOutOfBoundsException    in case the index provided is greater then the {@link #size()} - 1.
     */
    T get(int index);

    @Override
    default T head() {
        if (size() > 0) {
            return get(0);
        }

        throw new NoSuchElementException("Cannot get head on empty collection");
    }

    @Override
    Sequence<T> tail();

    /**
     * Reverse the order of the elements in the sequence.
     *
     * @return the reversed sequence
     */
    Sequence<T> reverse();

   @Override
    Sequence<T> filter(Predicate<T> predicate);

    @Override
    <U> Sequence<U> map(Function<T, U> mapper);

    /**
     * Transform this collection into one supported natively in Java.
     *
     * @return the native java collection
     */
    java.util.Collection<T> toJava();

}
