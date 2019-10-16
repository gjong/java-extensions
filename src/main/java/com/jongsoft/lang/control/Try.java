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
package com.jongsoft.lang.control;

import java.util.Objects;
import java.util.function.Function;

import com.jongsoft.lang.control.impl.TryFailure;
import com.jongsoft.lang.control.impl.TrySuccess;

/**
 * <p>
 *     The try interface allows for easier and more functional coding around exception handling. This interface allows for
 *     conditionally running a operation chain. The chain will continue to be executed for as long as there is no exception.
 *     As soon as an exception occurs the chain will be short-circuited and the resulting Try will contain an {@link #isFailure()}
 *     and the underlying {@link #getCause()}.
 * </p>
 * <p>
 *     The Try can be used like the example below, to either recover from an exception or gracefully catch one.
 * </p>
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code  String result = Try<String>supply(() -> {
 *          throw new Exception("not supported");
 *        })
 *        .recover(x -> "recovered")
 *        .get();
 * }</pre>
 *
 * @param <T>   the type of entity contained
 * @since 0.0.2
 */
public interface Try<T> {

    /**
     * Get the value contained in the try.
     *
     * This method will throw an exception in case the try failed somewhere in the chain. Use
     * {@link #isFailure()} to detect if a failure occurred.
     *
     * @return the value contained
     */
    T get();

    /**
     * Indicates if the try operation resulted in an exception
     *
     * @return true in case of an exception of the try, otherwise false
     */
    boolean isFailure();

    /**
     * Indicates if the try operation was successful
     *
     * @return false in case of an exception of the try, otherwise true
     */
    boolean isSuccess();
    
    /**
     * Return the cause of the failure. If the {@link #isSuccess()} is true this call will cause an exception.
     * 
     * @return get the root cause for a failure
     */
    Throwable getCause();

    /**
     * <p>
     *    Set a fallback operation to be executed when the primary operation fails.
     * </p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code List<String> safeGet = Try.supply(myDatabase::getRecords)
     *          .recover(x -> Collections.emptyList())
     *          .get();
     * }</pre>
     * <p>
     *   In the sample above a call is made to a database repository, which can fail with various exceptions. In case
     *   the call fails then the logic in the recover operation is executed instead. The result of this logic will be
     *   returned to the caller. In the case of the example the caller will get an empty list when the database call
     *   fails.
     * </p>
     *
     * @param recoverMethod the operation that will be executed when the primary operation fails
     * @param <X>           the type of exception thrown by the primary operation
     * @return              the result of the recoverMethod operation
     */
    <X extends Throwable> Try<T> recover(Function<X, T> recoverMethod);

    /**
     * Passes then entity contained within the {@link #get()} if the try has a success. Otherwise it will not call the
     * consumer and return the try containing the failure.
     * <p>
     * This method exists for chaining checked functions, like:
     * </p>
     * <pre>{@code  Try.of(() -> "my success")
     *     .andTry( str -> System.out.println(str));
     * }</pre>
     *
     * @param consumer the checked consumer to consume the value contained within
     *
     * @return a {@link Try} with {@link #isSuccess()} is true in case of no issues, otherwise a {@link Try} with
     *         {@link #isFailure()} is true.
     *
     * @throws NullPointerException in case the {@code consumer} is null
     */
    default Try<T> consume(CheckedConsumer<? super T> consumer) {
        Objects.requireNonNull(consumer, "Consumer cannot be null");
        if (!isFailure()) {
            try {
                consumer.accept(get());
            } catch (Exception th) {
                return new TryFailure<>(th);
            }
        }

        return this;
    }

    /**
     * Allows for secondary runners to be executed within a try..catch. If the runner does not throw any
     * exceptions then this method will return the current Try instance. Otherwise it will return a {@link Try} with
     * {@link #isFailure()} returning true.
     * <p>
     * This method allows for chaining multiple logical execution blocks that need to run sequential as long as there
     * are no exceptions.
     * </p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code  Try.of(() -> System.out.println("first") )
     *     .andTry(() -> System.out.println("second"));
     * }</pre>
     *
     * @param runner    the part of code that should be executed, wrapped in a {@link CheckedRunner}
     * @return {@link Try} with {@link #isSuccess()} is true in case of no issues, otherwise a {@link Try} with
     *         {@link #isFailure()} is true.
     *
     * @throws NullPointerException in case the <code>runner</code> is null
     */
    default Try<T> run(CheckedRunner runner) {
        Objects.requireNonNull(runner, "Runner cannot be null");
        if (!isFailure()) {
            try {
                runner.run();
            } catch (Exception th) {
                return new TryFailure<>(th);
            }
        }
        return this;
    }

    default <U> Try<U> map(Function<T, U> mapper) {
        Objects.requireNonNull(mapper, "Mapper cannot be null");
        if (!isFailure()) {
            try {
                return new TrySuccess<>(mapper.apply(get()));
            } catch (Exception th) {
                return new TryFailure<>(th);
            }
        }

        return new TryFailure<>(getCause());
    }

}
