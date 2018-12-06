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

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.jongsoft.lang.collection.tuple.Tuple;

/**
 * The {@link HashMap} allows for storing basic key, value pair based data.
 *
 * @param <K>   the type of the key
 * @param <T>   the type of the value
 */
public class HashMap<K, T> implements Map<K, T> {

    @SuppressWarnings("unchecked")
    private static final HashMap<?, ?> EMPTY = new HashMap(Array.empty());

    private Sequence<Tuple.Pair<K, T>> delegate;

    private HashMap(Sequence<Tuple.Pair<K, T>> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Map<K, T> put(final K key, final T value) {
        Objects.requireNonNull(key, "A null value is not allowed for the key in a map");

        Sequence<Tuple.Pair<K, T>> afterRemove = delegate;
        int existingEntry = delegate.firstIndexWhere(e -> Objects.equals(e.getFirst(), key));
        if (existingEntry > -1) {
            afterRemove = delegate.remove(existingEntry);
        }

        return new HashMap<>(afterRemove.append(Tuple.Pair.of(key, value)));
    }

    @Override
    public Map<K, T> remove(final K key) {
        Objects.requireNonNull(key, "A null value is not allowed for the key in a map");

        int indexOf = delegate.firstIndexWhere(e -> Objects.equals(e.getFirst(), key));
        if (indexOf > -1) {
            return new HashMap<>(delegate.remove(indexOf));
        }

        return this;
    }

    @Override
    public boolean containsValue(final T value) {
        for (int i = 0; i < delegate.size(); i++) {
            if (Objects.equals(delegate.get(i).getSecond(), value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public T get(final K key) {
        for (int i = 0; i < delegate.size(); i++) {
            if (Objects.equals(delegate.get(i).getFirst(), key)) {
                return delegate.get(i).getSecond();
            }
        }

        return null;
    }

    @Override
    public Tuple.Pair<K, T> head() {
        return delegate.head();
    }

    @Override
    public Map<K, T> tail() {
        return new HashMap<>(delegate.tail());
    }

    @Override
    public Map<K, T> filter(final Predicate<Tuple.Pair<K, T>> predicate) {
        return new HashMap<>(delegate.filter(predicate));
    }

    @Override
    public <U> Collection<U> map(final Function<Tuple.Pair<K, T>, U> mapper) {
        return delegate.map(mapper);
    }

    @Override
    public Stream<Tuple.Pair<K, T>> stream() {
        return delegate.stream();
    }

    @Override
    public Stream<T> valueStream() {
        return delegate.stream()
                .map(Tuple.Pair::getSecond);
    }

    @Override
    public Iterator<Tuple.Pair<K, T>> iterator() {
        return delegate.iterator();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public <U> U foldRight(final U start, final BiFunction<? super Tuple.Pair<K, T>, ? super U, ? extends U> combiner) {
        throw new IllegalStateException("not yet implemented");
    }

    @Override
    public java.util.Map<K, T> toJava() {
        java.util.Map<K, T> result = new java.util.HashMap<>(delegate.size());
        delegate.forEach(entry -> result.put(entry.getFirst(), entry.getSecond()));
        return result;
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    /**
     * Create a new empty {@link HashMap}.
     *
     * @param <K>   the type of the key
     * @param <T>   the type of the balue
     * @return      the new empty map
     */
    @SuppressWarnings("unchecked")
    public static <K, T> Map<K, T> create() {
        return (Map<K, T>) EMPTY;
    }

}
