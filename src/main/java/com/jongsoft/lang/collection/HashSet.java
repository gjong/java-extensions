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
package com.jongsoft.lang.collection;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.jongsoft.lang.collection.support.Collections;

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

    private static final HashSet EMPTY = new HashSet(new Object[0]);

    private HashSet(final Object[] delegate) {
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
    public Collector<T, ArrayList<T>, Set<T>> collector() {
        return Collections.collector(HashSet::of);
    }

    @Override
    protected <X> Supplier<Set<X>> emptySupplier() {
        return HashSet::empty;
    }

    @Override
    protected Function<Object[], Set<T>> wrapperSupplier() {
        return HashSet::new;
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    /**
     * Create an empty HashSet.
     *
     * @param <T>   the type of the entities
     * @return  the empty hash set
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> empty() {
        return EMPTY;
    }

    /**
     * Create a new hash set containing all unique elements.
     *
     * @param elements  the elements for the new set
     * @param <T>       the type of the elements
     * @return  the created set
     */
    @SafeVarargs
    public static <T> Set<T> of(T...elements) {
        Set<T> newSet = empty();

        for (int i = 0; i < elements.length; i++) {
            newSet = newSet.add(elements[i]);
        }

        return newSet;
    }

    /**
     * Create a new set containing all the elements of the {@code iterable}.
     *
     * @param iterable  the iterable containing the elements to add to the set
     * @param <T>       the type of the elements
     * @return  the created set
     * @throws NullPointerException if {@code iterable} is null
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> of(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (iterable instanceof HashSet) {
            return (HashSet<T>) iterable;
        }

        return new HashSet<>(Iterator.of(iterable).toNativeArray());
    }
}
