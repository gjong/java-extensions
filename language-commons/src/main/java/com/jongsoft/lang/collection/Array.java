package com.jongsoft.lang.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.Arrays.copyOf;

public class Array<T> implements List<T> {

    private static final Array<?> EMPTY = new Array<>(new Object[0]);

    private final T[] delegate;

    private Array(T[] delegate) {
        this.delegate = delegate;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Stream<T> stream() {
        return null;
    }

    @Override
    public List<T> insert(int index, T value) {

        return null;
    }

    public static <T> Collector<T, ArrayList<T>, Array<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, Array<T>> finisher = Array::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    private static <T> Array<T> create(T[] array) {
        return array.length == 0
                ? (Array<T>) EMPTY
                : new Array<>(array);
    }

    public static <T> Array<T> empty() {
        return (Array<T>) EMPTY;
    }

    public static <T> Array<T> of(T element) {
        return create((T[]) new Object[]{element});
    }

    /**
     * Creates a new {@link Array} with the provided elements as the contents.
     *
     * @param elements  the elements to add to the array
     * @param <T>       the type that the elements represent
     * @return          the new array list
     *
     * @throws NullPointerException in case the passed elements is null
     */
    public static <T> Array<T> of(T...elements) {
        Objects.requireNonNull(elements, "The provided elements cannot be null");
        return create(copyOf(elements, elements.length));
    }

    public static <T> Array<T> ofAll(Iterable<? extends T> elements) {
        return elements instanceof Array
                ? (Array<T>) elements
                : create(toArray(elements));
    }

    private static <T> T[] toArray(Iterable<T> elements) {
        if (elements instanceof java.util.List) {
            final java.util.List<T> list = (java.util.List<T>) elements;
            return (T[]) list.toArray();
        } else {
            final java.util.Iterator<? extends T> it = elements.iterator();
            final java.util.List<T> list = new java.util.ArrayList<>();
            while (it.hasNext()) {
                list.add(it.next());
            }
            return (T[]) list.toArray();
        }
    }
}
