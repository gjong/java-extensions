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
package com.jongsoft.lang.collection.support;

import com.jongsoft.lang.Collections;
import com.jongsoft.lang.collection.Iterator;
import com.jongsoft.lang.collection.Traversable;

import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Provides a common implementation for the {@link Iterator} implementing the following calls as a common:
 * <ul>
 *     <li>{@link #toString()}</li>
 *     <li>{@link #next()}}</li>
 * </ul>
 *
 * @param <T>   the type contained in the Iterator
 */
public abstract class AbstractIterator<T> implements Iterator<T> {

    protected abstract T getNext();

    @Override
    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No next element available in the iterator");
        }

        return getNext();
    }

    @Override
    public Iterator<T> filter(final Predicate<T> predicate) {
        return Collections.List(this)
                  .filter(predicate)
                  .iterator();
    }

    @Override
    public Iterator<T> reject(Predicate<T> predicate) {
        return filter(predicate.negate());
    }

    @Override
    public Traversable<T> orElse(final Supplier<? extends Iterable<? extends T>> supplier) {
        return hasNext() ? this : Collections.Iterator((Iterable<T>) supplier.get());
    }

    @Override
    public Traversable<T> orElse(final Iterable<? extends T> other) {
        return hasNext() ? this : Collections.Iterator((Iterable<T>) other);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
