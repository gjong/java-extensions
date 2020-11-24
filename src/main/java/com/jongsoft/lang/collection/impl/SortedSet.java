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

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import com.jongsoft.lang.API;
import com.jongsoft.lang.Collections;
import com.jongsoft.lang.collection.Set;

public class SortedSet<T> extends AbstractSet<T> implements Set<T> {

    private final transient Comparator<T> comparator;

    public SortedSet(Object[] delegate, Comparator<T> comparator) {
        super(delegate);

        Objects.requireNonNull(comparator, "The comparator cannot be null");
        this.comparator = comparator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> append(final T value) {
        Object[] existing = iterator().toNativeArray();

        int insertIndex = -1;
        for (int i = 0; i < existing.length; i++) {
            final int compareResult = comparator.compare((T) existing[i], value);

            if (compareResult == 0) {
                return this;
            } else if (compareResult > 0) {
                insertIndex = i;
                break;
            }
        }

        if (insertIndex == -1) {
            // in case of -1 then no entity has been found that is bigger according to the compareTo logic
            insertIndex = existing.length;
        }

        Object[] newDelegate = new Object[existing.length + 1];
        if (!isEmpty()) {
            System.arraycopy(existing, 0, newDelegate, 0, insertIndex);
            if (insertIndex < existing.length) {
                System.arraycopy(existing, insertIndex, newDelegate, insertIndex + 1, existing.length - insertIndex);
            }
        }
        newDelegate[insertIndex] = value;

        return new SortedSet<>(newDelegate, comparator);
    }

    @Override
    protected <X> Supplier<Set<X>> emptySupplier() {
        return Collections::SortedSet;
    }

    @Override
    protected Function<Object[], Set<T>> wrapperSupplier() {
        return x -> new SortedSet<>(x, comparator);
    }

    @Override
    public boolean contains(T element) {
        return exists(e -> comparator.compare(e, element) == 0);
    }

}
