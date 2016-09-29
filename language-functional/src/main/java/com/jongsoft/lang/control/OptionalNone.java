package com.jongsoft.lang.control;

import com.jongsoft.lang.core.None;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

class OptionalNone<T> extends None<T> implements Optional<T> {

    protected static final OptionalNone<?> NONE = new OptionalNone<>();

    private static final long serialVersionUID = 1L;

    @Override
    public T getOrSupply(Supplier<T> supplier) {
        return supplier.get();
    }

    @Override
    public <X extends Throwable> T getOrThrow(Supplier<X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier, "Exception supplier cannot be null");
        throw exceptionSupplier.get();
    }

    @Override
    public <U> Optional<U> map(Function<T, U> mapper) {
        return Optional.empty();
    }

    @Override
    public Optional<T> filter(Predicate<T> predicate) {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Optional<Empty>: " + super.toString();
    }
}
