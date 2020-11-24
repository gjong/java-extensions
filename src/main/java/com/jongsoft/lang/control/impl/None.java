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
package com.jongsoft.lang.control.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.jongsoft.lang.API;
import com.jongsoft.lang.Control;
import com.jongsoft.lang.control.Optional;

public class None<T> implements Optional<T> {

    public static final None<?> INSTANCE = new None<>();

    private static final long serialVersionUID = 1L;

    @Override
    public T get() {
        throw new NoSuchElementException("No value is present");
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public OrElse ifPresent(Consumer<T> consumer) {
        return Constants.OR_ELSE_EMPTY;
    }

    @Override
    public <X extends Throwable> OrElse ifPresent(Supplier<X> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier, "Supplier of exceptions cannot be null");
        return Constants.OR_ELSE_EMPTY;
    }

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
        return Control.Option(null);
    }

    @Override
    public Optional<T> filter(Predicate<T> predicate) {
        return this;
    }

    @Override
    public String toString() {
        return "Optional<Empty>: None";
    }

}
