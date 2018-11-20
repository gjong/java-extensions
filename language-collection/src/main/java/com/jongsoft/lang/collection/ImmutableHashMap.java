package com.jongsoft.lang.collection;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * The {@link ImmutableHashMap} allows for storing basic key, value pair based data.
 *
 * @param <K>   the type of the key
 * @param <T>   the type of the value
 */
public class ImmutableHashMap<K, T> implements ImmutableMap<K, T> {

    private static final ImmutableHashMap<?, ?> EMPTY = new ImmutableHashMap(Array.empty());

    private Sequence<Entry<K, T>> delegate;

    private ImmutableHashMap(Sequence<Entry<K, T>> delegate) {
        this.delegate = delegate;
    }

    @Override
    public ImmutableMap<K, T> put(final K key, final T value) {
        Objects.requireNonNull(key, "A null value is not allowed for the key in a map");

        final HashMapEntry<K, T> entry = new HashMapEntry<>(key, value);

        final Sequence<Entry<K, T>> afterRemove = delegate.remove(entry);
        return new ImmutableHashMap<>(afterRemove.add(entry));
    }

    @Override
    public ImmutableMap<K, T> remove(final K key) {
        int indexOf = delegate.indexOf(new HashMapEntry<>(key, null));
        if (indexOf > -1) {
            return new ImmutableHashMap<>(delegate.remove(indexOf));
        }

        return this;
    }

    @Override
    public boolean contains(final T value) {
        for (int i = 0; i < delegate.size(); i++) {
            if (Objects.equals(delegate.get(i).value(), value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public T get(final K key) {
        for (int i = 0; i < delegate.size(); i++) {
            if (Objects.equals(delegate.get(i).key(), key)) {
                return delegate.get(i).value();
            }
        }

        return null;

    }

    @Override
    public Stream<ImmutableMap.Entry<K, T>> stream() {
        return delegate.stream();
    }

    @Override
    public Stream<T> valueStream() {
        return delegate.stream()
                .map(Entry::value);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    /**
     * Create a new empty {@link ImmutableHashMap}.
     *
     * @param <K>   the type of the key
     * @param <T>   the type of the balue
     * @return      the new empty map
     */
    public static <K, T> ImmutableMap<K, T> create() {
        return (ImmutableMap<K, T>) EMPTY;
    }

    //------------------------------------------------------------------
    //-- Support class

    private class HashMapEntry<K, T> implements ImmutableMap.Entry<K, T> {

        private final K key;
        private final T value;

        private HashMapEntry(final K key, final T value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K key() {
            return key;
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof HashMapEntry) {
                return Objects.equals(key, ((HashMapEntry) obj).key);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }
}
