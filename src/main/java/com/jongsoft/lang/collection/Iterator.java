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
package com.jongsoft.lang.collection;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.jongsoft.lang.API;
import com.jongsoft.lang.Collections;
import com.jongsoft.lang.Control;
import com.jongsoft.lang.collection.support.AbstractIterator;
import com.jongsoft.lang.control.Optional;

/**
 * An extension on the default {@link java.util.Iterator} that adds utility operations to easily manipulate the iterator or locate elements
 * inside it.
 *
 * @param <T>   the type contained in the iterator
 * @since 0.0.3
 */
public interface Iterator<T> extends java.util.Iterator<T>, Traversable<T> {

    @Override
    Iterator<T> filter(Predicate<T> predicate);

    @Override
    default Optional<T> first(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "The predicate may not be null");

        while (hasNext()) {
            final T next = next();
            if (predicate.test(next)) {
                return Control.Option(next);
            }
        }

        return Control.Option(null);
    }

    @Override
    default <U> U foldLeft(U start, BiFunction<? super U, ? super T, ? extends U> combiner) {
        Objects.requireNonNull(combiner, "combiner is null");
        U x = start;
        for (T y : this) {
            x = combiner.apply(x, y);
        }

        return x;
    }

    @Override
    default <U> U foldRight(U start, BiFunction<? super T, ? super U, ? extends U> combiner) {
        return foldLeft(start, (y, x) -> combiner.apply(x, y));
    }

    /**
     * This operation is a convenience method for the {@link #next()}.
     *
     * @return the next value in the iterator
     * @see #next()
     */
    @Override
    default T get() {
        return next();
    }

    @Override
    default boolean isSingleValued() {
        return false;
    }

    @Override
    default Iterator<T> iterator() {
        return Collections.Iterator((T[])this.toNativeArray());
    }

    @Override
    default Optional<T> last(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "The predicate may not be null");

        T lastMatch = null;
        while (hasNext()) {
            final T next = next();
            if (predicate.test(next)) {
                lastMatch = next;
            }
        }

        return Control.Option(lastMatch);
    }

    @Override
    default <U> Iterator<U> map(Function<T, U> mapper) {
        if (hasNext()) {
            Iterator<T> pointer = this;
            return new AbstractIterator<>() {

                @Override
                public void reset() {
                    pointer.reset();
                }

                @Override
                protected U getNext() {
                    return mapper.apply(pointer.next());
                }

                @Override
                public boolean hasNext() {
                    return pointer.hasNext();
                }
            };
        }

        return Collections.Iterator();
    }

    @Override
    default T reduceLeft(BiFunction<? super T, ? super T, ? extends T> reducer) {
        Sequence<T> array = Collections.List(this);
        return array.tail().foldLeft(array.head(), reducer);
    }

    /**
     * Move the iterator back to the first element in the sequence.
     */
    void reset();

    /**
     * Create a primitive array of the elements contained within the iterator.
     *
     * @return the primitive array
     */
    @SuppressWarnings("unchecked")
    default Object[] toNativeArray() {
        int size = 0;
        while (hasNext()) {
            next();
            size++;
        }
        reset();

        Object[] clone = new Object[size];
        Arrays.setAll(clone, i -> next());
        return clone;
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    /**
     * Combine multiple iterator instances into one big iterator.
     *
     * @param iterators the iterators to be combined
     * @param <T>       the type of the iterator
     * @return the new iterator containing all elements of all iterators
     */
    @SafeVarargs
    static <T> Iterator<T> concat(final Iterator<T>...iterators) {
        return new AbstractIterator<>() {
            private int index = 0;

            @Override
            public void reset() {
                for (Iterator it : iterators) {
                    it.reset();
                }
                index = 0;
            }

            @Override
            protected T getNext() {
                if (!iterators[index].hasNext()) {
                    index++;
                }

                return iterators[index].next();
            }

            @Override
            public boolean hasNext() {
                return iterators[index].hasNext() || (index + 1 < iterators.length);
            }
        };
    }
}
