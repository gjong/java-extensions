package com.jongsoft.lang;

import com.jongsoft.lang.control.*;
import com.jongsoft.lang.control.impl.*;
import com.jongsoft.lang.time.Range;
import com.jongsoft.lang.time.impl.RangeImpl;

import java.time.temporal.Temporal;
import java.util.Objects;

public class Control {
    /**
     * This operation will compare the {@code left} with the provided {@code right} and return
     * the corresponding equality.
     *
     * @param left      the left value
     * @param right     the right value
     * @param <T>       the type for left
     * @param <X>       the type for right
     * @return          the equality between {@code left} and {@code right}
     */
    public static <T, X> Equal Equal(T left, X right) {
        return EqualHelper.IS_EQUAL.append(left, right);
    }

    /**
     * Creates a new {@link Optional} representing the provided value.
     * <p>
     * This method will allow <code>null</code> values.
     *
     * @param <T>       the type of the value
     * @param value     the actual value
     *
     * @return          an {@link Optional} representing the value
     */
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <T> Optional<T> Option(T value) {
        return value != null ? new Some<>(value) : (Optional<T>) None.INSTANCE;
    }

    /**
     * Will always return an empty optional.
     *
     * @param <T>       the type of the value
     * @return          the empty optional
     */
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <T> Optional<T> Option() {
        return (Optional<T>) None.INSTANCE;
    }

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
    @SuppressWarnings("squid:S00100")
    public static <T> Try<T> Try(CheckedSupplier<T> supplier) {
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
    @SuppressWarnings("squid:S00100")
    public static Try<Void> Try(CheckedRunner runner) {
        Objects.requireNonNull(runner, "Runner cannot be null");
        try {
            runner.run();
        } catch (Exception exception) {
            return new TryFailure<>(exception);
        }

        return new TrySuccess<>(null);
    }

}
