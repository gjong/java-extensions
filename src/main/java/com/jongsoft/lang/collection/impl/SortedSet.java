package com.jongsoft.lang.collection.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.Set;
import com.jongsoft.lang.collection.support.Collections;

public class SortedSet<T> extends AbstractSet<T> implements Set<T> {

    private transient final Comparator<T> comparator;

    public SortedSet(Object[] delegate, Comparator<T> comparator) {
        super(delegate);

        Objects.requireNonNull(comparator, "The comparator cannot be null");
        this.comparator = comparator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> add(final T value) {
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
        return API::SortedSet;
    }

    @Override
    protected Function<Object[], Set<T>> wrapperSupplier() {
        return x -> new SortedSet<>(x, comparator);
    }

    @Override
    public Collector<T, ArrayList<T>, Set<T>> collector() {
        return Collections.collector(x -> API.Set(comparator, x));
    }

    @Override
    public boolean contains(T element) {
        return exists(e -> comparator.compare(e, element) == 0);
    }

}
