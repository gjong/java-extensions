package com.jongsoft.lang.collection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.jongsoft.lang.collection.support.Collections;

public class SortedSet<T> extends AbstractSet<T> implements Set<T> {
    @SuppressWarnings("unchecked")
    private static final SortedSet EMPTY = new SortedSet(new Object[0], new EqualsComparator());

    private transient final Comparator<T> comparator;

    private SortedSet(Object[] delegate, Comparator<T> comparator) {
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
        return SortedSet::empty;
    }

    @Override
    protected Function<Object[], Set<T>> wrapperSupplier() {
        return x -> new SortedSet<>(x, comparator);
    }

    @Override
    public Collector<T, ArrayList<T>, Set<T>> collector() {
        return Collections.collector(x -> ofAll(comparator, x));
    }

    @Override
    public boolean contains(T element) {
        return exists(e -> comparator.compare(e, element) == 0);
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    /**
     * Create an empty SortedSet.
     *
     * @param <T>   the type of the entities
     * @return  the empty comparator based set
     */
    @SuppressWarnings("unchecked")
    public static <T> Set empty() {
        return (SortedSet<T>) EMPTY;
    }

    /**
     * Create a new Sorted Set with all the {@code elements} in it sorted by the provided {@code comparator}.
     *
     * @param comparator    the comparator to use for sorting the set
     * @param elements      the elements initially in the set
     * @param <T>           the type of the elements
     * @return              the newly generated sorted set
     */
    @SafeVarargs
    public static <T> Set<T> of(Comparator<T> comparator, T...elements) {
        return new SortedSet<>(elements, comparator);
    }

    /**
     * Creates a new Sorted Set with the provided {@code comparator} and all elements that are in the provided {@code iterable}.
     *
     * @param comparator    the comparator to use for sorting the set
     * @param iterable      the iterable whose elements to copy into the new set
     * @param <T>           the type of the elements
     * @return              the newly generated sorted set
     */
    public static <T> Set<T> ofAll(Comparator<T> comparator, Iterable<? extends T> iterable) {
        return new SortedSet<>(Iterator.of(iterable).toNativeArray(), comparator);
    }

    private static class EqualsComparator<T> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            Objects.requireNonNull(01, "cannot run compareTo on null value");
            Objects.requireNonNull(02, "cannot run compareTo on null value");

            if (o1 instanceof Comparable) {
                Comparable<T> comparable = (Comparable<T>) o1;
                return comparable.compareTo(o2);
            }

            return Objects.equals(o1, o2) ? 0 : 1;
        }

    }
}
