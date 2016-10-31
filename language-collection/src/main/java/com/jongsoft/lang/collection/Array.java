package com.jongsoft.lang.collection;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static java.lang.String.format;
import static java.util.Arrays.copyOf;

public class Array<T> implements List<T> {

    private static final Array<?> EMPTY = new Array<>(new Object[0]);

    private final T[] delegate;

    private Array(T[] delegate) {
        this.delegate = delegate;
    }

    @Override
    public int size() {
        return delegate.length;
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        if (index >= delegate.length || index < 0) {
            throw new IndexOutOfBoundsException(format("%s is not in the bounds of 0 and %s", index, delegate.length));
        }
        return delegate[index];
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(Arrays.asList(delegate)).iterator();
    }

    @Override
    public List<T> filter(Predicate<T> predicate) {
        return stream()
                .filter(predicate)
                .collect(collector());
    }

    @Override
    public List<T> insert(int index, T value) {
        T[] newDelegate = (T[]) new Object[delegate.length + 1];
        System.arraycopy(delegate, 0, newDelegate, 0, index);
        newDelegate[index] = value;
        System.arraycopy(delegate, index, newDelegate, index + 1, delegate.length - index);
        return create(newDelegate);
    }

    @Override
    public List<T> remove(int index) {
        T[] newDelegate = (T[]) new Object[delegate.length - 1];
        System.arraycopy(delegate, 0, newDelegate, 0, index - 1);
        System.arraycopy(delegate, index, newDelegate, index - 1, delegate.length - index);
        return create(newDelegate);
    }

    public static <T> Collector<T, ArrayList<T>, Array<T>> collector() {
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };

        return Collector.of(ArrayList::new, ArrayList::add, combiner, Array::ofAll);
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
