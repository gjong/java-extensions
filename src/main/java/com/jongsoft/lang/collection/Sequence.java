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

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 *   Sequences are ordered collections of elements.
 *   These collections allow for appending duplicate entries, but all entries will always be returned in the order that they were added or
 *   inserted.
 * </p>
 * <p></p>
 * <table>
 *     <caption><strong>Single change operations</strong></caption>
 *     <thead>
 *         <tr><td>Operation</td><td>Description</td></tr>
 *     </thead>
 *     <tbody>
 *         <tr><td>{@linkplain #append(Object)}</td><td>Add element to end of the sequence</td></tr>
 *         <tr><td>{@linkplain #union(Iterable)}</td><td>Add elements to end of the sequence</td></tr>
 *         <tr><td>{@linkplain #prepend(Object)}</td><td>Add elements to start of the sequence</td></tr>
 *         <tr><td>{@linkplain #insert(int, Object)}</td><td>Add an element to the indicated place</td></tr>
 *         <tr><td>{@linkplain #remove(Object)}</td><td>Remove the element</td></tr>
 *         <tr><td>{@linkplain #remove(int)}</td><td>Remove the element at the indicated place</td></tr>
 *     </tbody>
 * </table>
 * <p></p>
 * <table>
 *     <caption><strong>Set operations</strong></caption>
 *     <thead>
 *         <tr><td>Operation</td><td>Description</td></tr>
 *     </thead>
 *     <tbody>
 *         <tr><td>{@linkplain #union(Iterable)}</td><td>Combine this sequence of elements with the provided iterable</td></tr>
 *         <tr><td>{@linkplain #reject(Predicate)}</td><td>Create a new sequence without the rejected values matching the
 *         predicate</td></tr>
 *         <tr><td>{@linkplain #filter(Predicate)}</td><td>Create a new sequence with values matching the predicate</td></tr>
 *         <tr><td>{@linkplain #map(Function)}</td><td>Create a new sequence with the mapped values</td></tr>
 *         <tr><td>{@linkplain #distinct()}</td><td>Create a sequence with only unique elements</td></tr>
 *     </tbody>
 * </table>
 *
 * <p>
 *     <strong>Note:</strong> all operation that alter the contents of the sequence will return a new instance.
 * </p>
 *
 * @param <T>   the entity type of the sequence
 */
public interface Sequence<T> extends List<T> {

    @Override
    default Sequence<T> append(T value) {
        return insert(size(), value);
    }

    /**
     * Create a new sequence with the provided {@code value} at position 0 and the remainder of this sequence from position 1 to
     * {@linkplain #size()} + 1.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code  // will result in a sequence with 1, 2, 3, 4
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
     * Add an element to the list at the provided index, shifting all elements after the index one.
     *
     * @param index the index at which to insert the element
     * @param value the element to insert
     * @return      the updated list with the inserted element
     */
    Sequence<T> insert(int index, T value);

    @Override
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
    default Sequence<T> reject(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    /**
     * Generate a new sequence containing only unique elements from this collection.
     *
     * @return  a new unique sequence
     */
    Sequence<T> distinct();

    @Override
    <U> Sequence<U> map(Function<T, U> mapper);

    /**
     * Generate a new sequence using the {@code keyGenerator}.
     * Calling this operation will create a new map grouping the elements by the generator provided.
     * <p><strong>Example:</strong></p>
     * <pre>{@code  // This will result in Map(1 -> List(1, 11, 21), 2 -> List(2, 12, 22))
     *   List(1, 2, 11, 12, 21, 22)
     *      .groupBy(x -> x % 10);
     * }</pre>
     *
     * @param keyGenerator  the generator to use for creating keys
     * @param <K>           the type of the key
     * @return              the new map created using the generator
     * @throws NullPointerException if {@code keyGenerator} is null
     */
    <K> Map<K, Sequence<T>> groupBy(Function<? super T, ? extends K> keyGenerator);

    /**
     * Create a new sequence with all elements of this sequence combined with the elements of the provided iterable.
     * The elements in this sequence will be added before the provided {@code iterable} in the returned sequence.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code  // the example would be a List(1, 2, 3, 4)
     *   Sequence result = List(1, 2).union(Sequence(3, 4));
     * }</pre>
     *
     * @param iterable  the elements to be added
     * @return          the new list containing a union between this and the iterable
     */
    Sequence<T> union(Iterable<T> iterable);

    /**
     * Transform this collection into one supported natively in Java.
     *
     * @return the native java collection
     */
    java.util.List<T> toJava();

}
