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
package com.jongsoft.lang.control;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jongsoft.lang.common.control.CheckedConsumer;
import com.jongsoft.lang.common.control.CheckedRunner;
import com.jongsoft.lang.common.control.CheckedSupplier;
import com.jongsoft.lang.common.core.Presence;
import com.jongsoft.lang.control.impl.TryFailure;
import com.jongsoft.lang.control.impl.TrySuccess;

public interface Try<T> extends Presence<T> {

    /**
     * Attempt to execute code that will return an entity, but may also result into an exception.
     *
     * @param <T>      the type of entity that will be returned in case of success
     * @param supplier the supplier that will return the entity
     *
     * @return either an {@link Try} with success set to true and a get returning the entity, or a {@link Try} with
     *         failure set to true and the get throwing the exception.
     *
     * @throws NullPointerException in case the supplier is null
     */
    static <T> Try<T> supply(CheckedSupplier<T> supplier) {
        Objects.requireNonNull(supplier, "Supplier cannot be null");
        try {
            return new TrySuccess<>(supplier.get());
        } catch (Exception exception) {
            return new TryFailure<>(exception);
        }
    }

    /**
     * Attempt to execute the code in the {@link CheckedRunner}. If the execution results into an exception then this
     * call will return a {@link Try} with a failure result. Otherwise it will return an empty success {@link Try}.
     *
     * @param runner the code to execute with a try..catch construction
     *
     * @return A {@link Try} with success set to true if the execution went without exceptions, otherwise it will return
     *         a {@link Try} with a failure set to true.
     *
     * @throws NullPointerException in case the runner is null
     */
    static Try<Void> run(CheckedRunner runner) {
        Objects.requireNonNull(runner, "Runner cannot be null");
        try {
            runner.run();
        } catch (Exception exception) {
            return new TryFailure<>(exception);
        }

        return new TrySuccess<>(null);
    }

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
     * Convenience method for checked consumer call.
     * 
     * @param consumer the consumer function to apply
     * @see #andTry(CheckedConsumer)
     * @return a {@link Try} with {@link #isSuccess()} is true in case of no issues, otherwise a {@link Try} with
     *         {@link #isFailure()} is true.
     */
    default Try<T> and(Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "Consumer cannot be null");
        return andTry(consumer::accept);
    }

    /**
     * Execute the runnable with a try and catch around it. This is a convenience operation for {@link #andTry(CheckedRunner)}.
     *
     * @see #andTry(CheckedRunner)
     * @param runner the runner to execute
     * @return a {@link Try} with {@link #isSuccess()} is true in case of no issues, otherwise a {@link Try} with
     *         {@link #isFailure()} is true.
     */
    default Try<T> and(Runnable runner) {
        Objects.requireNonNull(runner, "Runner cannot be null");
        return andTry(runner::run);
    }

    /**
     * Passes then entity contained within the {@link #get()} if the try has a success. Otherwise it will not call the
     * consumer and return the try containing the failure.
     * <p>
     * This method exists for chaining checked functions, like:
     * </p>
     * <pre>
     *      Try.of( () -&gt; "my success")
     *         .andTry( str -&gt; System.out.println(str));
     * </pre>
     *
     * @param consumer the checked consumer to consume the value contained within
     *
     * @return a {@link Try} with {@link #isSuccess()} is true in case of no issues, otherwise a {@link Try} with
     *         {@link #isFailure()} is true.
     *
     * @throws NullPointerException in case the {@code consumer} is null
     */
    default Try<T> andTry(CheckedConsumer<? super T> consumer) {
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
     * <pre>
     *     Try.of( () -&gt; System.out.println("first") )
     *         .andTry( () -&gt; System.out.println("second"));
     * </pre>
     *
     * @param runner    the part of code that should be executed, wrapped in a {@link CheckedRunner}
     * @return {@link Try} with {@link #isSuccess()} is true in case of no issues, otherwise a {@link Try} with
     *         {@link #isFailure()} is true.
     *
     * @throws NullPointerException in case the <code>runner</code> is null
     */
    default Try<T> andTry(CheckedRunner runner) {
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
