package com.jongsoft.lang.collection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class SortedSet<T> extends AbstractSet<T> implements Set<T> {
    @SuppressWarnings("unchecked")
    private static final SortedSet EMPTY = new SortedSet(new Object[0], new EqualsComparator());

    private final Comparator<T> comparator;

    private SortedSet(Object[] delegate, Comparator<T> comparator) {
        super(delegate);

        Objects.requireNonNull(comparator, "The comparator cannot be null");
        this.comparator = comparator;
    }

    @Override
    protected <X> Supplier<Set<X>> emptySupplier() {
        return SortedSet::empty;
    }

    @Override
    protected Function<Object[], Set<T>> wrapperSupplier() {
        return x -> new SortedSet(x, comparator);
    }

    @Override
    public <Y> Collector<Y, ArrayList<Y>, Set<Y>> collector() {
        return null;
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
    public static <T> SortedSet empty() {
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
    public static <T> SortedSet<T> of(Comparator<T> comparator, T...elements) {
        return new SortedSet<>(elements, comparator);
    }

    private static class EqualsComparator<T> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            return Objects.equals(o1, o2) ? 0 : 1;
        }

    }
}
