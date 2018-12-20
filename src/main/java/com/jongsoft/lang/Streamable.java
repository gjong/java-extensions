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
package com.jongsoft.lang;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * This interface indicates that the class implements the {@link #stream()} method. Allowing access to the Java 8 streaming API.
 *
 * @param <T> the type of entities that are contained in the {@link Stream}.
 * @since 0.0.1
 */
public interface Streamable<T> extends Iterable<T> {

    /**
     * Creates a {@link Stream} to access the entities of type T contained within this {@link Streamable} class.
     *
     * @return the {@link Stream} entity.
     */
    Stream<T> stream();

    /**
     * Perform a mapping operation on the elements in the stream.
     * This operation will loop over all elements in the stream and apply the {@code mapper} method. The mapped values will be returned
     * in as a new set of elements.
     *
     * @param <U>       the type of object expected as a result
     * @param mapper    the mapping functionality
     * @return          the mapped object
     */
    <U> Streamable<U> map(Function<T, U> mapper);

    /**
     * Filter out an element if it does not match the supplied {@code predicate}.
     * This operation will iterate over all elements and return a new set containing only the elements where the predicate returns {@code
     * true} for.
     *
     * @param predicate the predicate to apply to the contents of this
     * @return the filtered value
     * @throws NullPointerException if {@code predicate} is null
     */
    Streamable<T> filter(Predicate<T> predicate);

}
