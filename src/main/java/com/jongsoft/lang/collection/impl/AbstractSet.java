package com.jongsoft.lang.collection.impl;

import static java.lang.String.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.jongsoft.lang.collection.Iterator;
import com.jongsoft.lang.collection.Set;

abstract class AbstractSet<T> implements Set<T> {

    private final Object[] delegate;

    AbstractSet(Object[] delegate) {
        this.delegate = delegate;
    }

    @Override
    public Set<T> add(final T value) {
        if (contains(value)) {
            return this;
        }

        Object[] newDelegate = new Object[delegate.length + 1];
        System.arraycopy(delegate, 0, newDelegate, 0, delegate.length);
        newDelegate[delegate.length] = value;
        return this.wrapperSupplier().apply(newDelegate);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Set<T> remove(final int index) {
        validateOutOfBounds(index);
        Object[] clone = new Object[delegate.length - 1];

        System.arraycopy(delegate, 0, clone, 0, index);
        System.arraycopy(delegate, index  + 1, clone, index, delegate.length - index - 1);

        return this.wrapperSupplier().apply(clone);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public int firstIndexOf(final Predicate<T> predicate) {
        for (int i = 0; i < size(); i++) {
            if (predicate.test(get(i))) {
                return i;
            }
        }

        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(final int index) {
        validateOutOfBounds(index);
        return (T) delegate[index];
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Set<T> tail() {
        if (size() == 0) {
            throw new NoSuchElementException("Cannot call tail on empty collection");
        } else if (size() == 1) {
            return this.<T>emptySupplier().get();
        }

        Object[] tail = new Object[delegate.length - 1];
        System.arraycopy(delegate, 1, tail, 0, delegate.length - 1);
        return this.wrapperSupplier().apply(tail);
    }

    @Override
    public Set<T> filter(final Predicate<T> predicate) {
        return stream()
                .filter(predicate)
                .collect(collector());
    }

    @Override
    @SuppressWarnings("Duplicates")
    public <U> Set<U> map(final Function<T, U> mapper) {
        Objects.requireNonNull(mapper, "The mapper cannot be null for this operation.");

        Set<U> mappedSet = this.<U>emptySupplier().get();
        for (int i = 0; i < size(); i++) {
            mappedSet = mappedSet.add(mapper.apply(get(i)));
        }

        return mappedSet;
    }

    @Override
    public int size() {
        return delegate.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return Iterator.of((T[]) delegate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public java.util.Set<T> toJava() {
        java.util.Set<T> result = new java.util.HashSet<>(delegate.length);
        for (Object o : delegate) {
            result.add((T) o);
        }
        return result;
    }

    private void validateOutOfBounds(int index) {
        if (index >= delegate.length || index < 0) {
            throw new IndexOutOfBoundsException(format("%s is not in the bounds of 0 and %s", index, delegate.length));
        }
    }

    protected abstract <X> Supplier<Set<X>> emptySupplier();
    protected abstract Function<Object[], Set<T>> wrapperSupplier();
    public abstract Collector<T, ArrayList<T>, Set<T>> collector();

}
