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

import java.util.Objects;
import java.util.function.Function;

import com.jongsoft.lang.API;
import com.jongsoft.lang.control.Try;
import com.jongsoft.lang.exception.NonFatalException;

public class TryFailure<T> extends None<T> implements Try<T> {

    private final NonFatalException cause;

    public TryFailure(Throwable exception) {
        Objects.requireNonNull(exception, "attempted to create failure without valid exception");
        cause = NonFatalException.of(exception);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X extends Throwable> Try<T> recover(Function<X, T> recoverMethod) {
        Objects.requireNonNull(recoverMethod, "The recover method cannot be null");
        return API.Try(() -> recoverMethod.apply((X) cause.getCause()));
    }

    @Override
    public T get() {
        throw cause;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public Throwable getCause() {
        return cause.getCause();
    }

    @Override
    public <U> Try<U> map(final Function<T, U> mapper) {
        return Try.super.map(mapper);
    }
}
