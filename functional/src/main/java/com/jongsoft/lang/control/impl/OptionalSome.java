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
package com.jongsoft.lang.control.impl;

import com.jongsoft.lang.control.Optional;
import com.jongsoft.lang.core.impl.Some;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OptionalSome<T> extends Some<T> implements Optional<T> {

    private static final long serialVersionUID = 1L;

    public OptionalSome(T value) {
        super(value);
    }

    @Override
    public T getOrSupply(Supplier<T> supplier) {
        return super.get();
    }

    @Override
    public <X extends Throwable> T getOrThrow(Supplier<X> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier, "Exception supplier cannot be null");
        return super.get();
    }

    @Override
    public <U> Optional<U> map(Function<T, U> mapper) {
        Objects.requireNonNull(mapper, "Mapping function cannot be null");
        return Optional.ofNullable(mapper.apply(get()));
    }

    @Override
    public Optional<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "Predicate may not be null");
        return this.all(predicate) ? this : Optional.empty();
    }

    @Override
    public String toString() {
        return "Optional<Some>: " + super.toString();
    }
}
