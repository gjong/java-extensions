/*
 * The MIT License
 *
 * Copyright 2016 Jong Soft.
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
package com.jongsoft.lang.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.jongsoft.lang.common.core.OrElse;
import com.jongsoft.lang.common.core.Presence;

public class None<T> implements Presence<T> {
    private static final long serialVersionUID = 1L;
    
    private static final None<?> NONE = new None<>();

    protected None() {
    }
    
    @Override
    public T get() {
        throw new NoSuchElementException("Value is empty");
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
        return OrElse.empty();
    }

    @Override
    public <X extends Throwable> OrElse ifPresent(Supplier<X> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier, "Supplier of exceptions cannot be null");
        return OrElse.empty();
    }

    @Override
    public <U> Presence<U> map(final Function<T, U> mapper) {
        return none();
    }

    @Override
    public String toString() {
        return "None";
    }
    
    //----------------------------------------------------------------------------------------------
    //-- All static helper methods to instantiate the None class
    
    @SuppressWarnings("unchecked")
    public static <T> Presence<T> none() {
        return (None<T>) NONE;
    }

}
