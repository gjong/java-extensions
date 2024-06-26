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

import com.jongsoft.lang.API;
import com.jongsoft.lang.Collections;
import com.jongsoft.lang.collection.tuple.Pair;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p>
 *   Sequences are ordered collections of elements.
 *
 *   These collections allow for appending duplicate entries, but all entries will always be returned in the order
 *   that they were added or inserted.
 * </p>
 * <p>
 *   Creating a new sequence can be achieved by using the on of the following operations:
 * </p>
 * <ul>
 *   <li>{@link com.jongsoft.lang.Collections#List(Iterator)}</li>
 *   <li>{@link com.jongsoft.lang.Collections#List(Iterable)}</li>
 *   <li>{@link com.jongsoft.lang.Collections#List(Object[])}</li>
 *   <li>{@link com.jongsoft.lang.Collections#List(Object)}</li>
 * </ul>
 * <table>
 *     <caption><strong>Single change operations</strong></caption>
 *     <thead>
 *         <tr><td>Operation</td><td>Description</td></tr>
 *     </thead>
 *     <tbody>
 *         <tr><td>{@linkplain #append(Object)}</td><td>Add element to end of the sequence</td></tr>
 *         <tr><td>{@linkplain #prepend(Object)}</td><td>Add elements to start of the sequence</td></tr>
 *         <tr><td>{@linkplain #insert(int, Object)}</td><td>Add an element to the indicated place</td></tr>
 *         <tr><td>{@linkplain #remove(Object)}</td><td>Remove the element</td></tr>
 *         <tr><td>{@linkplain #remove(int)}</td><td>Remove the element at the indicated place</td></tr>
 *     </tbody>
 * </table>
 * <table>
 *     <caption><strong>Collection operations</strong></caption>
 *     <thead>
 *         <tr><td>Operation</td><td>Description</td></tr>
 *     </thead>
 *     <tbody>
 *         <tr><td>{@linkplain #union(Iterable)}</td><td>Combine this sequence of elements with the provided iterable</td></tr>
 *         <tr><td>{@linkplain #reject(Predicate)}</td><td>Create a new sequence without the rejected values matching the
 *         predicate</td></tr>
 *         <tr><td>{@linkplain #retain(Iterable)}</td><td>Create a new sequence with only the elements in both sets</td></tr>
 *         <tr><td>{@linkplain #filter(Predicate)}</td><td>Create a new sequence with values matching the predicate</td></tr>
 *         <tr><td>{@linkplain #map(Function)}</td><td>Create a new sequence with the mapped values</td></tr>
 *         <tr><td>{@linkplain #distinct()}</td><td>Create a set with only unique elements</td></tr>
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

    @Override
    Sequence<T> replace(int index, T replacement);

    @Override
    Sequence<T> replaceIf(Predicate<T> predicate, T replacement);

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
    <K> Map<K, ? extends Sequence<T>> groupBy(Function<? super T, ? extends K> keyGenerator);

    @Override
    default Sequence<T> reject(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    @Override
    default Pair<? extends Sequence<T>, ? extends Sequence<T>> split(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Map<Boolean, ? extends Sequence<T>> pairs = groupBy(predicate::test);
        return API.Tuple(pairs.get(true), pairs.get(false));
    }

    /**
     * Generate a new set containing only unique elements from this collection. As identified by their
     * {@link Object#hashCode()}.
     *
     * @return  a set with unique elements
     */
    Set<T> distinct();

    @Override
    Set<T> distinctBy(Comparator<T> comparator);

    @Override
    <U> Sequence<U> map(Function<T, U> mapper);

    @Override
    default Sequence<T> orElse(Iterable<? extends T> other) {
        return isEmpty() ? Collections.List(other) : this;
    }

    @Override
    default Sequence<T> orElse(Supplier<? extends Iterable<? extends T>> supplier) {
        return isEmpty() ? Collections.List(supplier.get()) : this;
    }

    /**
     * Transform this collection into one supported natively in Java.
     *
     * @return the native java collection
     */
    java.util.List<T> toJava();

    @Override
    Sequence<T> sorted();

    @Override
    Sequence<T> union(Iterable<T> iterable);

    @Override
    Sequence<T> retain(Iterable<T> iterable);
}
