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

import java.util.function.Function;
import java.util.function.Predicate;

import com.jongsoft.lang.common.core.Value;
import com.jongsoft.lang.control.Optional;

public interface Collection<T> extends Iterable<T>, Value<T> {

    /**
     * Filter the list contents with the provided predicate. Only returning those elements that match the predicate.
     *
     * @param predicate the predicate to use in the filter operation
     * @return          the filtered list
     */
    Collection<T> filter(Predicate<T> predicate);

    /**
     * Get the amount of elements contained in the collection.
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
     * Find the first match in the Iterator using the provided {@link Predicate}.
     *
     * @param predicate the predicate to use
     * @return          the first match found
     * @throws NullPointerException in case that the predicate is null
     */
    default Optional<T> findFirst(Predicate<T> predicate) {
        return iterator().first(predicate);
    }

    /**
     * Find the last match in the Iterator using the provided {@link Predicate}.
     *
     * @param predicate the predicate to use
     * @return          the last match found
     * @throws NullPointerException in case that the predicate is null
     */
    default Optional<T> findLast(Predicate<T> predicate) {
        return iterator().last(predicate);
    }

    @Override
    <U> Collection<U> map(Function<T, U> mapper);

    @Override
    Iterator<T> iterator();
}
