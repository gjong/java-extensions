package com.jongsoft.lang.control;

public final class Control {

    public static <T> Optional<T> Option(T value) {
        return Optional.ofNullable(value);
    }

    public static <T> Try<T> Try(CheckedSupplier<T> supplier) {
        return Try.supply(supplier);
    }

}
