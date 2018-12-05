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

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The Presence interface provides key features required for detecting if a value is present in the wrapper entity. An example
 * implementation of this is the purely functional optional.
 * <p>
 *     The class provides some basic operations to allow determination if a value is present or not.
 * </p>
 *
 * @param <T>   the type of the entity wrapped
 * @since 0.0.3
 */
public interface Presence<T> extends Value<T> {

    @Override
    default boolean isSingleValued() {
        return true;
    }

    /**
     * Indicates if a value is present within the wrapper
     *
     * @return true if an element is present, otherwise false
     */
    boolean isPresent();

    /**
     * Process the present element wrapped within using the provided {@link java.util.function.Consumer}.
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

}
