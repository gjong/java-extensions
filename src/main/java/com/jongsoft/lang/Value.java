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
package com.jongsoft.lang;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The Value interface represents a simple wrapped value. This interface can contain exactly one entity inside, or None in case of an empty
 * value.
 *
 * @param <T>   the type of object being wrapped
 * @since 0.0.1
 */
public interface Value<T> extends Streamable<T>, Serializable {
    
    /**
     * Return the contents {@code T} of the wrapped value.
     * <p>
     * This can throw a {@link NoSuchElementException} if no value is present.
     *
     * @return returns the object wrapped in this value
     * @throws NoSuchElementException   if no element is present
     */
    T get();

    /**
     * Is this instance single-valued or not.
     *
     * @return {@code true} if single-valued, otherwise {@code false}
     */
    boolean isSingleValued();

    /**
     * Create a stream of the value.
     *
     * @return the stream containing the value
     */
    @Override
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
    
    /**
     * Validate if the value contains the provided item.
     *
     * @param element   an element of type T, can be <code>null</code>
     * @return          true, if the element is contained within
     */
    default boolean contains(T element) {
        return exists(e -> Objects.equals(e, element));
    }
    
    /**
     * Validate that all elements match the predicate provided.
     * This can be useful to quickly look over the elements to verify that they all meet a pre-specified criteria.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code    // result will be true
     *    List("a", "b").all(x -> x.length() == 1);
     * }</pre>
     * 
     * @param predicate the predicate to test with
     * @return          true if all elements match the predicate, otherwise false
     *
     * @see #none(Predicate)
     * @see #exists(Predicate)
     * @throws NullPointerException in case the {@link Predicate} is null
     */
    default boolean all(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate provided cannot be null");
        return !exists(predicate.negate());
    }

    /**
     * Validate that none of the elements match the {@code predicate} provided.
     *
     * @param predicate the predicate to test with
     * @return          true if none of the elements match, otherwise false
     *
     * @see #all(Predicate)
     * @see #exists(Predicate)
     * @throws NullPointerException in case the {@link Predicate} is null
     */
    default boolean none(Predicate<? super T> predicate) {
        return !exists(predicate);
    }

    /**
     * Validate if an element is contained that matches the provided predicate.
     *
     * @param predicate the predicate to validate with
     * @return          true, if any element is contained within that matches the predicate
     * @throws NullPointerException in case the {@link Predicate} is null
     */
    default boolean exists(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate provided cannot be null");
        for (T entity : this) {
            if (predicate.test(entity)) {
                return true;
            }
        }
        return false;
    }

}
