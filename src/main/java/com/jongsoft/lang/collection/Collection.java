/*
 * The MIT License
 *
 * Copyright 2016-2020 Jong Soft.
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
import java.util.function.Supplier;

import com.jongsoft.lang.collection.tuple.Pair;
import com.jongsoft.lang.control.Optional;

/**
 * The collection interface enables basic operations that allow access to the elements.
 *
 * @param <T> the type of the elements
 */
public interface Collection<T> extends Traversable<T> {

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
    Collection<T> filter(Predicate<T> predicate);

    @Override
    default Optional<T> first(Predicate<T> predicate) {
        return iterator().first(predicate);
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
     * Convenience method to see if the current list is empty or not.
     * This method will yield the same result as checking if the {@linkplain #size()} returns the value 0.
     *
     * @return true if the list contains elements, otherwise false
     * @see #size()
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    default boolean isSingleValued() {
        return false;
    }

    @Override
    default Optional<T> last(Predicate<T> predicate) {
        return iterator().last(predicate);
    }

    @Override
    <U> Collection<U> map(Function<T, U> mapper);

    @Override
    Collection<T> orElse(Iterable<? extends T> other);

    @Override
    Collection<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);

    @Override
    default T reduceLeft(BiFunction<? super T, ? super T, ? extends T> reducer) {
        Objects.requireNonNull(reducer, "reducer is null");
        return iterator().reduceLeft(reducer);
    }

    @Override
    Collection<T> reject(Predicate<T> predicate);

    /**
     * The split operation is an execution that combines the {@link #reject(Predicate)} and the {@link #filter(Predicate)}
     * methods into one. Separating the values into 2 separate buckets.
     *
     * The first bucket will contain the same as the {@link #filter(Predicate)} operation. The second bucket will
     * contain the result of the {@link #reject(Predicate)} operation.
     *
     * @param predicate the predicate to use
     * @return the two buckets of this operation
     */
    Pair<? extends Collection<T>, ? extends Collection<T>> split(Predicate<T> predicate);

    /**
     * Get the amount of elements contained in the collection.
     *
     * @return 0 if empty, otherwise the amount of entries
     */
    int size();

    /**
     * Sum all elements using the provided accumulator. This reduces the Collection to a single numbered value.
     *
     * @param accumulator the accumulator to be used
     * @return            the resulting value
     * @throws NullPointerException in case {@code accumulator} is null
     */
    default long summing(BiFunction<Long, T, Long> accumulator) {
        Objects.requireNonNull(accumulator, "accumulator is null");
        return foldLeft(0L, accumulator);
    }

    /**
     * Build a new collection with all elements except for the {@linkplain #head() head}.
     * If there is only one element present then an empty collection will be returned. If the operation is called on an empty collection an
     * {@code NoSuchElementException} will be thrown.
     *
     * @return a collection containing the tail
     * @throws java.util.NoSuchElementException if the collection is {@code #isEmpty}
     */
    Collection<T> tail();
}
