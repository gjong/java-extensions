package com.jongsoft.lang.common.core;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.jongsoft.lang.common.Runner;

public interface Presence<T> extends Value<T> {

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
