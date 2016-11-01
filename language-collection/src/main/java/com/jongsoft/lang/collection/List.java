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

import com.jongsoft.lang.Streamable;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface List<T> extends Iterable<T>, Streamable<T> {

    /**
     * Add an element to the end of the contents of the current list and return the result in a new {@link List} instance
     *
     * @param value     the value to append to the list
     * @return          the new list with the value appended
     */
    default List<T> add(T value) {
        return insert(size(), value);
    }

    List<T> insert(int index, T value);

    /**
     * Find the index for the provided element, will return <code>-1</code> if the element
     * is not present in the list.
     *
     * @param lookFor   the element to look for
     * @return  the index of the element, or <code>-1</code> if none found
     */
    int indexOf(Object lookFor);

    /**
     * Removes an element from the list and returns a new instance of the list.
     *
     * @param index     the index of the element to be removed
     * @return          the new instance of the list without the element at the provided index
     * @throws IndexOutOfBoundsException    in case the index is not between the 0 and list size
     */
    List<T> remove(int index);

    /**
     * Removes the first element found matching the provided value. The match is done based upon the
     * {@link java.util.Objects#equals(Object, Object)} call.
     *
     * @param value the element to be removed
     * @return the current list if the element is not present, otherwise a new list instance without the element in it.
     */
    default List<T> remove(T value) {
        int idx = indexOf(value);
        if (idx > -1) {
            return remove(idx);
        }

        return this;
    }

    /**
     * Attempts to look in the list if a value is present or not. This method will use the {@link Object#equals(Object)}
     * to determine equality.
     *
     * @param lookFor   the object to look for in the list
     * @return          <code>true</code> if the object exists in the list
     */
    default boolean contains(Object lookFor) {
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            if ((lookFor == null && iterator.next() == null) || (lookFor != null &&lookFor.equals(iterator.next()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Provides the amount of entries present in the list
     *
     * @return 0 if empty, otherwise the amount of entries
     */
    int size();

    /**
     * Convenience method to see if the current list is empty or not.
     *
     * @return true if the list contains elements, otherwise false
     */
    default boolean isEmpty() {
        return size() > 0;
    }

    /**
     * Get the element at the location of <code>index</code>
     *
     * @param index     the index of the element in the list to get
     * @return          the element at the provided index
     * @throws IndexOutOfBoundsException    in case the index provided is greater then the {@link #size()} - 1.
     */
    T get(int index) throws IndexOutOfBoundsException;

    @Override
    default Stream<T> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.CONCURRENT), false);
    }

    List<T> filter(Predicate<T> predicate);

}
