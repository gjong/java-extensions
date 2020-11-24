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
package com.jongsoft.lang.collection.impl;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.jongsoft.lang.API;
import com.jongsoft.lang.Collections;
import com.jongsoft.lang.collection.Collection;
import com.jongsoft.lang.collection.Foldable;
import com.jongsoft.lang.collection.Set;

/**
 * This class implements the {@link Set} interface and ensures uniqueness of the elements using there {@link Object#hashCode()}.
 * There is no specific order of the elements in the set and the order may change over time. This implementation does not support {@code
 * null} values.
 *
 * @param <T> the type of the elements
 * @see Set
 * @see Collection
 * @see Foldable
 * @since 0.0.2
 */
public class HashSet<T> extends AbstractSet<T> implements Set<T> {

    public HashSet(final Object[] delegate) {
        super(delegate);
    }

    @Override
    public boolean contains(final T element) {
        Objects.requireNonNull(element, "A set cannot contain a null value");
        int entityHash = element.hashCode();
        return exists(e -> e.hashCode() == entityHash);
    }

    @Override
    public <U> U foldRight(final U start, final BiFunction<? super T, ? super U, ? extends U> combiner) {
        Objects.requireNonNull(combiner, "combiner is null");
        return foldLeft(start, (x , y) -> combiner.apply(y, x));
    }

    @Override
    protected <X> Supplier<Set<X>> emptySupplier() {
        return Collections::Set;
    }

    @Override
    protected Function<Object[], Set<T>> wrapperSupplier() {
        return HashSet::new;
    }

}
