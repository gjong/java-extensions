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
package com.jongsoft.lang.control;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.jongsoft.lang.Filterable;
import com.jongsoft.lang.Runner;
import com.jongsoft.lang.Value;

/**
 * The Optional provides a functional way to detect <code>null</code> values without null reference checks or complicated
 * logic throughout the code base.
 * <p>
 *     Usage of this Optional is advices as a return type rather then using <code>null</code> as it moves away the change
 *     of potential {@link NullPointerException} in the code calling the operation.
 * </p>
 * <pre> {@code  // Sample usage of the Optional
 *     API.Option("one")
 *          .ifPresent(System.out::println)
 *          .orElse(() -> System.out.println("No value is present");
 * }</pre>
 *
 * @param <T> the type of entity contained in the Optional
 * @since 0.0.1
 */
public interface Optional<T> extends Value<T>, Filterable<T> {
    
    @Override
    <U> Optional<U> map(Function<T, U> mapper);

    @Override
    Optional<T> filter(Predicate<T> predicate);


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

    /**
     * This method will provide the entity contained within the {@link Optional}, in case no entity
     * is present the {@link Supplier} is called to create the else situation.
     *
     * @param supplier  the supplier to create an entity of none is contained in the {@link Optional}
     * @return          either the contained entity or the created one using the {@link Supplier}
     */
    T getOrSupply(Supplier<T> supplier);

    /**
     * This method will return the contained entity within the {@link Optional}, in case no entity
     * is present an exception will be thrown using the provided <code>exceptionSupplier</code>.
     *
     * @param exceptionSupplier     the {@link Supplier} used to create the {@link Throwable}
     * @param <X>                   the type of the Throwable that will be thrown
     * @return                      the entity contained within the {@link Optional}
     * @throws X                    in case of no entity
     * @throws NullPointerException in case the exceptionSupplier was null
     */
    <X extends Throwable> T getOrThrow(Supplier<X> exceptionSupplier) throws X;
    
}
