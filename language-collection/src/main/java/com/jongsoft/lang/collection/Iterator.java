/*
 * The MIT License
 *
 * Copyright 2018 Jong Soft.
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

import java.util.Objects;
import java.util.function.Predicate;

import com.jongsoft.lang.collection.support.AbstractIterator;
import com.jongsoft.lang.control.Optional;

/**
 * An extension on the default {@link java.util.Iterator} that adds utility operations to easily manipulate the iterator or locate elements
 * inside it.
 *
 * @param <T>   the type contained in the iterator
 */
public interface Iterator<T> extends java.util.Iterator<T> {

    /**
     * Find the last match in the Iterator using the provided {@link Predicate}.
     *
     * @param predicate the predicate to use
     * @return          the last match found
     * @throws NullPointerException in case that the predicate is null
     */
    default Optional<T> findLast(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "The predicate may not be null");

        T lastMatch = null;
        while (hasNext()) {
            final T next = next();
            if (predicate.test(next)) {
                lastMatch = next;
            }
        }

        return Optional.ofNullable(lastMatch);
    }

    /**
     * Find the first match in the Iterator using the provided {@link Predicate}.
     *
     * @param predicate the predicate to use
     * @return          the first match found
     * @throws NullPointerException in case that the predicate is null
     */
    default Optional<T> findFirst(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "The predicate may not be null");

        while (hasNext()) {
            final T next = next();
            if (predicate.test(next)) {
                return Optional.ofNullable(next);
            }
        }

        return Optional.empty();
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    /**
     * Creates a Iterator that contains all the provided elements.
     *
     * @param elements  the elements to wrap in an iterator
     * @param <T>       the type of the elements
     * @return          the new iterator
     */
    static <T> Iterator<T> of(T...elements) {
        return new AbstractIterator<>() {
            private int index = 0;

            @Override
            protected T getNext() {
                return elements[index++];
            }

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }
        };
    }

    static <T> Iterator<T> concat(Iterator<T>...iterators) {
        return new AbstractIterator<>() {
            private int index = 0;

            @Override
            protected T getNext() {
                if (!iterators[index].hasNext()) {
                    index++;
                }

                return iterators[index].next();
            }

            @Override
            public boolean hasNext() {
                return iterators[index].hasNext() || (index + 1 < iterators.length);
            }
        };
    }
}
