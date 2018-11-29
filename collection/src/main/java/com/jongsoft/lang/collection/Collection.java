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

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.jongsoft.lang.common.core.Value;
import com.jongsoft.lang.control.Optional;

public interface Collection<T> extends Iterable<T>, Value<T>, Foldable<T> {

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
        return size() == 0;
    }

    /**
     * Find the first match in the Iterator using the provided {@link Predicate}.
     *
     * @param predicate the predicate to use
     * @return          the first match found
     * @throws NullPointerException in case that the predicate is null
     */
    default Optional<T> first(Predicate<T> predicate) {
        return iterator().first(predicate);
    }

    /**
     * Find the last match in the Iterator using the provided {@link Predicate}.
     *
     * @param predicate the predicate to use
     * @return          the last match found
     * @throws NullPointerException in case that the predicate is null
     */
    default Optional<T> last(Predicate<T> predicate) {
        return iterator().last(predicate);
    }

    /**
     * Checks if all elements in the provided iterable are contained in this collection.
     * <p>
     *   This is a convenience method that uses the {@link #contains(Object)} to verify existence.
     * </p>
     *
     * @param elements  the elements that should be present
     * @return          true if all elements are present, false otherwise
     * @see #contains(Object)
     */
    default boolean containsAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        for (T element : elements) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    default <U> U foldLeft(U start, BiFunction<? super U, ? super T, ? extends U> combiner) {
        Objects.requireNonNull(combiner, "combiner is null");
        return iterator().foldLeft(start, combiner);
    }

    @Override
    default <U> U foldRight(U start, BiFunction<? super T, ? super U, ? extends U> combiner) {
        Objects.requireNonNull(combiner, "combiner is null");
        return iterator().foldRight(start, combiner);
    }

    @Override
    default T reduceLeft(BiFunction<? super T, ? super T, ? extends T> reducer) {
        Objects.requireNonNull(reducer, "reducer is null");
        return iterator().reduceLeft(reducer);
    }

    /**
     * Convert the contenst of the collection using the provided mapping function. A new collection containing the converted entities will
     * be returned.
     *
     * @param mapper    the mapping functionality
     * @param <U>       the target entity type
     * @return          a new collection containing the mapped entities
     */
    @Override
    <U> Collection<U> map(Function<T, U> mapper);

    @Override
    Iterator<T> iterator();

    /**
     * Fetch the head of the collection and return it. If the collection does not contain any elements then {@code null} will be returned.
     *
     * @return          returns the first element in the collection
     */
    @Override
    default T get() {
        return head();
    }

    /**
     * Returns the first element of a non empty collection
     *
     * @return the first element
     * @throws java.util.NoSuchElementException if the collection is {@code #isEmpty}
     */
    T head();

    /**
     * Return a collection containing all elements except the {@linkplain #head()}.
     *
     * @return a collection containing the tail
     * @throws java.util.NoSuchElementException if the collection is {@code #isEmpty}
     */
    Collection<T> tail();
}
