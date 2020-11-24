package com.jongsoft.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The ValueType class represents an immutable wrapper around a data entity. The value type
 * cannot be modified.
 *
 * @param <T>   the contained data type
 */
public class ValueType<T> implements Value<T> {

    private final T value;

    public ValueType(T value) {
        Objects.requireNonNull(value, "The data for a value type cannot be null");

        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean isSingleValued() {
        return true;
    }

    @Override
    public Value<T> filter(Predicate<T> predicate) {
        return predicate.test(value) ?
                this :
                Collections.List();
    }

    @Override
    public <U> Value<U> map(Function<T, U> mapper) {
        return new ValueType<>(mapper.apply(value));
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.Iterator(value);
    }

}
