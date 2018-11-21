/*
 * The MIT License
 *
 * Copyright 2018 Jong Soft.
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

import java.util.stream.Stream;

import com.jongsoft.lang.collection.tuple.Tuple;

/**
 * This class represents a map implementation that is immutable. This means all operations that would change its contents result into a
 * new {@link ImmutableMap} being returned. The current instance is not modified.
 *
 * @param <K>   the type for the key
 * @param <T>   the type for the values
 */
public interface ImmutableMap<K, T> extends Collection<Tuple.Pair<K, T>> {

    /**
     * Add a new entry to the {@link ImmutableMap}.
     *
     * @param key   the key for the entry
     * @param value the value for the entry
     * @return      the new {@link ImmutableMap} instance with the added key, value pair
     *
     * @throws NullPointerException in case the key is <code>null</code>
     */
    ImmutableMap<K, T> put(K key, T value);

    /**
     * Removes any element with the corresponding key from the map.
     *
     * @param key   the key indicating what needs to be removed
     * @return      a new instance without the entry with the key
     */
    ImmutableMap<K, T> remove(K key);

    /**
     * Returns true if this {@link ImmutableMap} contains the key provided.
     *
     * @param key   the key to look for
     * @return      true if found, otherwise false
     */
    default boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Verify if the presented value is contained within the {@link ImmutableMap} value set.
     *
     * @param value the value to look for
     * @return      if the value is found or not
     */
    boolean containsValue(T value);

    /**
     * Get the value from the {@link ImmutableMap} with the corresponding key. If the key is not contained in this instance then
     * <code>null</code> will be returned.
     *
     * @param key   the key to obtain the value for
     * @return      the corresponding value, or <code>null</code>
     */
    T get(K key);

    /**
     * Build a stream of the values contained within this {@link ImmutableMap}.
     *
     * @return      the stream with all the values
     */
    Stream<T> valueStream();

}
