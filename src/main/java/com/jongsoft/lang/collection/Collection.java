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

import com.jongsoft.lang.Value;
import com.jongsoft.lang.control.Optional;

/**
 * The collection interface enables basic operations that allow access to the elements.
 *
 * @param <T> the type of the elements
 */
public interface Collection<T> extends Value<T>, Foldable<T> {

    /**
     * Filter the list contents with the provided predicate. Only returning those elements that match the predicate.
     *
     * @param predicate the predicate to use in the filter operation
     * @return          the filtered list
     */
    Collection<T> filter(Predicate<T> predicate);

    /**
     * Return a list that removes all elements that match the {@code predicate} provided.
     *
     * @param predicate the predicate to use
     * @return          the elements that do not match the {@code predicate}
     * @throws NullPointerException in case {@code predicate} is null
     */
    Collection<T> reject(Predicate<T> predicate);

    /**
     * Convert the elements of the collection using the provided mapping function.
     *
     * This operation will not mutate the current collection but return a new one instead.
     *
     * @param mapper    the mapping functionality
     * @param <U>       the target entity type
     * @return          a new collection containing the mapped entities
     */
    @Override
    <U> Collection<U> map(Function<T, U> mapper);

    /**
     * Get the amount of elements contained in the collection.
     *
     * @return 0 if empty, otherwise the amount of entries
     */
    int size();

    /**
     * Convenience method to see if the current list is empty or not.
     * This method will yield the same result as checking if the {@linkplain #size()} returns the value 0.
     *
     * @return true if the list contains elements, otherwise false
     * @see #size()
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Find the first match in the elements using the provided {@link Predicate}.
     * The returned {@linkplain Optional} is {@code null} safe and will either contain the element or be an empty {@link Optional}.
     * <p></p>
     * <p><strong>Example:</strong></p>
     * <pre>{@code // the result will be an Optional with the value 2
     *    int firstMatch = Collection(1, 2, 3, 4)
     *          .first(i -> i % 2 == 0);}</pre>
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
     * The returned {@linkplain Optional} is {@code null} safe and will either contain the element or be an empty {@link Optional}.
     * <p></p>
     * <p><strong>Example:</strong></p>
     * <pre>{@code // the result will be an Optional with the value 4
     *    int firstMatch = Collection(1, 2, 3, 4)
     *          .last(i -> i % 2 == 0);}</pre>
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

    /**
     * Count all elements that match the provided predicate.
     *
     * @param predicate the predicate that must be true
     * @return          the amount of elements matching the predicate
     * @throws NullPointerException in case {@code predicate} is null
     */
    default int count(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return foldLeft(0, (count, element) -> predicate.test(element) ? count + 1 : count);
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

    @Override
    Iterator<T> iterator();

    /**
     * Fetch the head of the collection and return it.
     * The following operations will all return the same value for a non empty collection.
     *
     * <pre>{@code   Collection(0, 1).get();
     *   Collection(0, 1).head();
     * }</pre>
     *
     * The caller must verify the collection is not empty using either {@linkplain #size()} or {@linkplain #isEmpty()} to prevent a
     * {@code NoSuchElementException} to be thrown.
     *
     * @return the first element in the collection
     * @see #head()
     * @throws java.util.NoSuchElementException when there are no elements
     */
    @Override
    default T get() {
        return head();
    }

    /**
     * Fetch the head of the collection and return it.
     * The following operations will all return the same value for a non empty collection.
     *
     * <pre>{@code   Collection(0, 1).get();
     *   Collection(0, 1).head();
     * }</pre>
     *
     * The caller must verify the collection is not empty using either {@linkplain #size()} or {@linkplain #isEmpty()} to prevent a
     * {@code NoSuchElementException} to be thrown.
     *
     * @return the first element in the collection
     * @throws java.util.NoSuchElementException if the collection is {@code #isEmpty}
     */
    T head();

    /**
     * Build a new collection with all elements except for the {@linkplain #head() head}.
     * If there is only one element present then an empty collection will be returned. If the operation is called on an empty collection an
     * {@code NoSuchElementException} will be thrown.
     *
     * @return a collection containing the tail
     * @throws java.util.NoSuchElementException if the collection is {@code #isEmpty}
     */
    Collection<T> tail();

    @Override
    default boolean isSingleValued() {
        return false;
    }
}
