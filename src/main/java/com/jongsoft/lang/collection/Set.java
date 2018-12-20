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

import com.jongsoft.lang.collection.impl.HashSet;

/**
 * The sets are implementations of {@link Collection} that guarantee uniqueness in the collection. This will prevent duplicate entries.
 * How this is done varies pending the implementation. Currently the following implementations are supported:
 * <ul>
 *     <li>{@link HashSet}, an implementation that uses the entities hash</li>
 * </ul>
 *
 * @param <T>   the entity type contained in the set
 * @since 0.0.3
 */
public interface Set<T> extends List<T> {

    @Override
    Set<T> append(T value);

    @Override
    Set<T> remove(int index);

    @Override
    default T head() {
        if (size() > 0) {
            return get(0);
        }

        throw new NoSuchElementException("Cannot get head on empty collection");
    }

    @Override
    Set<T> tail();

    @Override
    Set<T> filter(Predicate<T> predicate);

    @Override
    default Set<T> reject(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    @Override
    <U> Set<U> map(Function<T, U> mapper);

    /**
     * Transform this collection into one supported natively in Java.
     *
     * @return the native java collection
     */
    java.util.Set<T> toJava();

    /**
     * Create a set that contains all elements that are contained in both {@code this} and the provided {@code iterable}.
     * Where the resulting set contains only unique elements.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code  // the example would be a Set(1, 2, 3, 4)
     *   Set result = Set(1, 2, 3).union(Set(3, 4));
     * }</pre>
     *
     * @param iterable  the iterable to perform the union with
     * @return  the product of the union operation
     */
    Set<T> union(Iterable<T> iterable);

    /**
     * Creates a set that contains only the elements contained in both {@code this} and the provided {@code iterable}.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code  // the example would be a Set(3)
     *   Set result = Set(1, 2, 3).intersect(Set(3, 4));
     * }</pre>
     *
     * @param iterable  the iterable to perform the intersects with
     * @return  the product of the intersect operation
     */
    Set<T> intersect(Iterable<T> iterable);

    /**
     * Creates a new set that contains elements that are only in {@code this}, but not contained within {@code iterable}.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code  // the example would be a Set(1, 2)
     *   Set result = Set(1, 2, 3).complement(Set(3, 4));
     * }</pre>
     *
     * @param iterable  the iterable to perform the complement with
     * @return  the product of the complement operation
     */
    Set<T> complement(Iterable<T> iterable);

}
