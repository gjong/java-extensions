/*
 * The MIT License
 *
 * Copyright 2016 Jong Soft.
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
package com.jongsoft.lang.common.core;

import com.jongsoft.lang.common.Runner;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @param <T>   the type of object being wrapped
 */
public interface Value<T> extends Iterable<T>, Serializable {
    
    /**
     * Return the contents <code>T</code> of the wrapped value.
     * <p>
     * This can throw a {@link NoSuchElementException} if no value is present.
     *
     * @return returns the object wrapped in this value
     * @throws NoSuchElementException   if no element is present
     */
    T get();

    /**
     * Indicates if a value is present within the wrapper
     * 
     * @return true if an element is present, otherwise false
     */
    boolean isPresent();

    /**
     * Process the present element wrapped within using the provided {@link Consumer}.
     *
     * @param consumer   the method that will consume the element
     * @return           the {@link OrElse} functionality, which enables processing in case of {@link #isPresent() }
     *                   being false.
     */
    OrElse ifPresent(Consumer<T> consumer);

    /**
     * Throw an exception if an element is present within. Otherwise it will return the {@link OrElse}.
     *
     * @param exceptionSupplier the supplier to create the exception
     * @param <X>               the type of exception expected
     * @return                  the {@link OrElse} in case no element is present
     * @throws X                the exception thrown if a present element
     */
    <X extends Throwable> OrElse ifPresent(Supplier<X> exceptionSupplier) throws X;

    /**
     * Execute the runner method if no entity is present in this wrapped object
     *
     * @param runner                the code to execute if nothing is present
     * @throws NullPointerException in case the runner is null
     */
    default void ifNotPresent(Runner runner) {
        Objects.requireNonNull(runner, "Runner cannot be null");
        if (!isPresent()) {
            runner.run();
        }
    }

    /**
     * Throws an exception when no value is present in this value
     *
     * @param exceptionSupplier the supplier to create the exception
     * @param <X>               the type of exception expected
     * @throws X                the exception thrown if no present element
     * @throws NullPointerException in case the exceptionSupplier is null
     */
    default <X extends Throwable> void ifNotPresent(Supplier<X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier, "Supplier of exceptions cannot be null");
        if (!isPresent()) {
            throw exceptionSupplier.get();
        }
    }
    
    /**
     * Create a stream of the value.
     *
     * @return
     */
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
        return is(e -> Objects.equals(e, element));
    }
    
    /**
     * Validate that all elements contained within match the predicate provided.
     * 
     * @param predicate the predicate to test with
     * @return          true if all elements match the predicate, otherwise false
     */
    default boolean all(Predicate<? super T> predicate) {
        return !is(predicate.negate());
    }

    /**
     * Validate if an element is contained that matches the provided predicate.
     *
     * @param predicate the predicate to validate with
     * @return          true, if any element is contained within that matches the predicate
     * @throws NullPointerException in case the {@link Predicate} is null
     */
    default boolean is(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate provided cannot be null");
        for (T entity : this) {
            if (predicate.test(entity)) {
                return true;
            }
        }
        return false;
    }

}
