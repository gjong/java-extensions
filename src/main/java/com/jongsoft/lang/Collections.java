package com.jongsoft.lang;

import com.jongsoft.lang.collection.*;
import com.jongsoft.lang.collection.impl.*;
import com.jongsoft.lang.collection.support.AbstractIterator;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.Arrays.copyOf;

@SuppressWarnings("java:S100")
public class Collections {
    private Collections() {}

    private static final Array<?> EMPTY_LIST = new Array<>(new Object[0]);
    @SuppressWarnings("rawtypes")
    private static final HashSet EMPTY_SET = new HashSet(new Object[0]);
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static final SortedSet EMPTY_SORTED_SET = new SortedSet(new Object[0], new EqualsComparator());
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static final HashMap<?, ?> EMPTY_MAP = new HashMap(EMPTY_LIST);

    public static <T> Tree<T> Tree(String label, T rootValue) {
        return new TreeSet<>(label, rootValue);
    }

    public static <T> Tree<T> Tree(String label, T value, Iterable<Tree<T>> children) {
        return new TreeSet<>(label, value, children);
    }

    /**
     * Creates an {@link Sequence} containing exactly one element, being the one passed to this call.
     *
     * @param element the element to add to the new array
     * @param <T>     the type of the element
     * @return the new array list
     */
    @SuppressWarnings("squid:S00100")
    public static <T> Sequence<T> List(T element) {
        return create(new Object[]{element});
    }

    /**
     * Creates a new {@link Sequence} with the provided elements as the contents.
     *
     * @param elements the elements to add to the array
     * @param <T>      the type that the elements represent
     * @return the new array list
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    @SuppressWarnings("squid:S00100")
    public static <T> Sequence<T> List(final T... elements) {
        Objects.requireNonNull(elements, "The provided elements cannot be null");
        return create(copyOf(elements, elements.length));
    }

    /**
     * Creates a new {@link Sequence} with all the elements contained in the {@link Iterable}.
     *
     * @param elements the elements that should be in the new array
     * @param <T>      the type of the elements
     * @return the new array
     */
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <T> Sequence<T> List(Iterable<? extends T> elements) {
        return elements instanceof Array
                ? (Array<T>) elements
                : create(toArray(elements));
    }

    /**
     * Create a new {@link Sequence} containing all the elements in the {@code iterator}.
     *
     * @param iterator the iterator to copy into the array
     * @param <T>      the type of the elements
     * @return the newly created array
     * @throws NullPointerException if {@code iterator} is null
     */
    @SuppressWarnings("squid:S00100")
    public static <T> Sequence<T> List(Iterator<T> iterator) {
        Objects.requireNonNull(iterator, "iterator is null");
        return create(iterator.toNativeArray());
    }

    /**
     * Create a new {@link Sequence} that is filled using the provided supplier.
     *
     * @param numberOfElement the number of elements to create a list with
     * @param fillWith        the initial filling of the list
     * @param <T>             the type of the element
     * @return the newly created and filled list
     * @since 1.2.0
     */
    public static <T> Sequence<T> List(int numberOfElement, Supplier<T> fillWith) {
        return create(IntStream.range(0, numberOfElement)
                .mapToObj(index -> fillWith.get())
                .toArray());
    }

    /**
     * Create a new set containing all unique elements.
     *
     * @param elements the elements for the new set
     * @param <T>      the type of the elements
     * @return the created set
     */
    @SafeVarargs
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <T> Set<T> Set(T... elements) {
        Set<T> newSet = EMPTY_SET;

        for (int i = 0; i < elements.length; i++) {
            newSet = newSet.append(elements[i]);
        }

        return newSet;
    }

    /**
     * Create a new set containing all the elements of the {@code iterable}.
     *
     * @param iterable the iterable containing the elements to add to the set
     * @param <T>      the type of the elements
     * @return the created set
     * @throws NullPointerException if {@code iterable} is null
     */
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <T> Set<T> Set(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (iterable instanceof HashSet) {
            return (HashSet<T>) iterable;
        }

        Set<? extends T> set = new HashSet<>(Collections.<T>Iterator().toNativeArray());
        return (Set<T>) set.union(create(toArray(iterable)));
    }

    /**
     * Create a new sorted set with all the {@code elements} in it sorted by the provided {@code comparator}.
     *
     * @param comparator the comparator to use for sorting the set
     * @param elements   the elements initially in the set
     * @param <T>        the type of the elements
     * @return the newly generated sorted set
     */
    @SafeVarargs
    @SuppressWarnings("squid:S00100")
    public static <T> Set<T> Set(Comparator<T> comparator, T... elements) {
        return Collections.Set(comparator, create(elements));
    }

    /**
     * Creates a new Sorted Set with the provided {@code comparator} and all elements that are in the provided {@code iterable}.
     *
     * @param comparator the comparator to use for sorting the set
     * @param iterable   the iterable whose elements to copy into the new set
     * @param <T>        the type of the elements
     * @return the newly generated sorted set
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> Set(Comparator<T> comparator, Iterable<? extends T> iterable) {
        Set<T> set = new SortedSet<>(Collections.<T>Iterator().toNativeArray(), comparator);
        return set.union((Iterable<T>) iterable);
    }

    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <T> Set<T> SortedSet() {
        return EMPTY_SORTED_SET;
    }

    /**
     * Create a new empty {@link Map}.
     *
     * @param <K> the type of the key
     * @param <T> the type of the balue
     * @return the new empty map
     */
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <K, T> Map<K, T> Map() {
        return (Map<K, T>) EMPTY_MAP;
    }

    /**
     * Create a new map with one pair in it being the provided {@code key}, {@code value}.
     *
     * @param key   the key use for the first element
     * @param value the value use for the first element
     * @param <K>   the type of the key
     * @param <T>   the type of the balue
     * @return the new map
     */
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <K, T> Map<K, T> Map(K key, T value) {
        return (Map<K, T>) Map().put(key, value);
    }

    /**
     * Creates a Iterator that contains all the provided elements.
     *
     * @param elements the elements to wrap in an iterator
     * @param <T>      the type of the elements
     * @return the new iterator
     */
    @SafeVarargs
    public static <T> Iterator<T> Iterator(T... elements) {
        return new AbstractIterator<>() {
            private int index = 0;

            @Override
            public void reset() {
                index = 0;
            }

            @Override
            protected T getNext() {
                return elements[index++];
            }

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }
        };
    }

    /**
     * Create a new Iterator wrapping the provided iterable.
     *
     * @param iterable the iterable to wrap
     * @param <T>      the type of elements in the iterable
     * @return the iterator wrapping the iterable
     * @throws NullPointerException if {@code iterable} is null
     */
    public static <T> Iterator<T> Iterator(final Iterable<T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        return new AbstractIterator<>() {
            transient java.util.Iterator<T> original = iterable.iterator();

            @Override
            public boolean hasNext() {
                return original.hasNext();
            }

            @Override
            public void reset() {
                original = iterable.iterator();
            }

            @Override
            protected T getNext() {
                return original.next();
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <T> Sequence<T> create(Object[] array) {
        return array.length == 0 ? (Sequence<T>) EMPTY_LIST : new Array<>(array);
    }

    @SuppressWarnings("unchecked")
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

    private static class EqualsComparator<T> implements Comparator<T> {

        @Override
        @SuppressWarnings("unchecked")
        public int compare(T o1, T o2) {
            Objects.requireNonNull(o1, "cannot run compareTo on null value");
            Objects.requireNonNull(o2, "cannot run compareTo on null value");

            if (o1 instanceof Comparable) {
                Comparable<T> comparable = (Comparable<T>) o1;
                return comparable.compareTo(o2);
            }

            return Objects.equals(o1, o2) ? 0 : 1;
        }

    }
}
