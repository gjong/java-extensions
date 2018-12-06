package com.jongsoft.lang.control;

import com.jongsoft.lang.control.impl.None;
import com.jongsoft.lang.control.impl.Some;

public final class Control {

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
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> Option(T value) {
        return value != null ? new Some<>(value) : (Optional<T>) None.INSTANCE;

    }

    public static <T> Try<T> Try(CheckedSupplier<T> supplier) {
        return Try.supply(supplier);
    }

    public static Try<Void> Try(CheckedRunner runner) {
        return Try.run(runner);
    }

}
