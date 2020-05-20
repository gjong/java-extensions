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

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.jongsoft.lang.collection.tuple.Pair;

/**
 * This class represents a map implementation that is immutable. This means all operations that would change its contents result into a
 * new {@link Map} being returned. The current instance is not modified.
 *
 * @param <K>   the type for the key
 * @param <T>   the type for the values
 * @since 0.0.3
 */
public interface Map<K, T> extends Collection<Pair<K, T>> {

    /**
     * Add a new entry to the {@link Map}.
     *
     * @param key   the key for the entry
     * @param value the value for the entry
     * @return      the new {@link Map} instance with the added key, value pair
     *
     * @throws NullPointerException in case the key is <code>null</code>
     */
    Map<K, T> put(K key, T value);

    /**
     * Removes any element with the corresponding key from the map.
     *
     * @param key   the key indicating what needs to be removed
     * @return      a new instance without the entry with the key
     */
    Map<K, T> remove(K key);

    @Override
    Map<K, T> orElse(Supplier<? extends Iterable<? extends Pair<K, T>>> supplier);

    @Override
    Map<K, T> orElse(Iterable<? extends Pair<K, T>> other);

    /**
     * Returns true if this {@link Map} contains the key provided.
     *
     * @param key   the key to look for
     * @return      true if found, otherwise false
     */
    default boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Verify if the presented value is contained within the {@link Map} value set.
     *
     * @param value the value to look for
     * @return      if the value is found or not
     */
    boolean containsValue(T value);

    /**
     * Get the value from the {@link Map} with the corresponding key. If the key is not contained in this instance then
     * <code>null</code> will be returned.
     *
     * @param key   the key to obtain the value for
     * @return      the corresponding value, or <code>null</code>
     */
    T get(K key);

    @Override
    Map<K, T> tail();

    /**
     * Build a stream of the values contained within this {@link Map}.
     *
     * @return      the stream with all the values
     */
    Stream<T> valueStream();

    @Override
    Map<K, T> filter(Predicate<Pair<K, T>> predicate);

    @Override
    default Map<K, T> reject(Predicate<Pair<K, T>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    /**
     * Convert the map to a regular Java map type.
     *
     * @return  the new java map
     */
    java.util.Map<K, T> toJava();
}
