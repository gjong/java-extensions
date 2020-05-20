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
package com.jongsoft.lang.collection;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 * @param <T>   the entity type of the sequence
 * @since 0.0.5
 */
public interface List<T> extends Collection<T> {

    /**
     * Create a new list containing all elements in this instance and appending the provided {@code value} to the end of the new list.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code  // will result in a list with 2, 3, 4, 1
     *    List(2, 3, 4).append(1)
     * }</pre>
     *
     * @param value     the value to append to the list
     * @return          the new list with the value appended
     */
    List<T> append(T value);

    /**
     * Search the collections for the first element matching the provided {@link Predicate} and return the index
     * position of that element.
     *
     * @param predicate the predicate to match
     * @return  index of the found element, -1 if none found
     */
    int firstIndexWhere(Predicate<T> predicate);

    @Override
    List<T> filter(Predicate<T> predicate);

    /**
     * Get the element at the location of <code>index</code>
     *
     * @param index     the index of the element in the list to get
     * @return          the element at the provided index
     * @throws IndexOutOfBoundsException if {@code index} is greater then the {@link #size()} - 1
     */
    T get(int index);

    /**
     * Generate a new sequence using the {@code keyGenerator}.
     * Calling this operation will create a new map grouping the elements by the generator provided.
     * <p><strong>Example:</strong></p>
     * <pre>{@code  // This will result in Map(1 -> List(1, 11, 21), 2 -> List(2, 12, 22))
     *   Sequence(1, 2, 11, 12, 21, 22)
     *      .groupBy(x -> x % 10);
     * }</pre>
     *
     * @param keyGenerator  the generator to use for creating keys
     * @param <K>           the type of the key
     * @return              the new map created using the generator
     * @throws NullPointerException if {@code keyGenerator} is null
     */
    <K> Map<K, List<T>> groupBy(Function<? super T, ? extends K> keyGenerator);

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

    @Override
    <U> List<U> map(Function<T, U> mapper);

    @Override
    List<T> orElse(Iterable<? extends T> other);

    @Override
    List<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);

    /**
     * Create a pipeline for the current list. A pipeline can be used to create a set of lazy operations on the list
     * that will only get executed once the pipe is terminated.
     *
     * @return the pipeline for this list
     */
    Pipeline<T> pipeline();

    @Override
    List<T> reject(Predicate<T> predicate);

    /**
     * Removes an element from the list and returns a new instance of the list.
     *
     * @param index     the index of the element to be removed
     * @return          the new instance of the list without the element at the provided index
     * @throws IndexOutOfBoundsException  if {@code index} is not between the 0 and {@linkplain #size()}
     */
    List<T> remove(int index);

    @Override
    List<T> tail();

    /**
     * Create a new sequence with all elements of this sequence combined with the elements of the provided iterable.
     * The elements in this sequence will be added before the provided {@code iterable} in the returned sequence.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code  // the example would be a List(1, 2, 3, 4)
     *   List<Integer> result = List(1, 2).union(List(3, 4));
     * }</pre>
     *
     * @param iterable  the elements to be added
     * @return          the new list containing a union between this and the iterable
     */
    List<T> union(Iterable<T> iterable);
}
