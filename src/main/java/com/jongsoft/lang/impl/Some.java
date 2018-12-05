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
package com.jongsoft.lang.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.jongsoft.lang.OrElse;
import com.jongsoft.lang.Presence;

public class Some<T> implements Presence<T> {

    private static final long serialVersionUID = 1L;
    
    private final transient T value;

    protected Some(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.singletonList(value).iterator();
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public OrElse ifPresent(Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "Consumer cannot be null");
        consumer.accept(get());
        return OrElse.notEmpty();
    }

    @Override
    public <X extends Throwable> OrElse ifPresent(Supplier<X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier, "Supplier of exceptions cannot be null");
        throw exceptionSupplier.get();
    }

    @Override
    public <U> Presence<U> map(final Function<T, U> mapper) {
        Objects.requireNonNull(mapper, "The mapper cannot be null");

        return some(mapper.apply(value));
    }

    @Override
    public String toString() {
        return Objects.toString(value);
    }

    //----------------------------------------------------------------------------------------------
    //-- All static helper methods to instantiate the Some class
    
    public static <T> Presence<T> some(T value) {
        return new Some<>(value);
    }
}
