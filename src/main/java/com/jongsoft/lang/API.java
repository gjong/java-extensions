package com.jongsoft.lang;

import static java.util.Arrays.*;

import java.util.Comparator;
import java.util.Objects;

import com.jongsoft.lang.collection.impl.HashMap;
import com.jongsoft.lang.collection.Iterator;
import com.jongsoft.lang.collection.Map;
import com.jongsoft.lang.collection.Sequence;
import com.jongsoft.lang.collection.Set;
import com.jongsoft.lang.collection.impl.Array;
import com.jongsoft.lang.collection.impl.HashSet;
import com.jongsoft.lang.collection.impl.SortedSet;
import com.jongsoft.lang.control.CheckedRunner;
import com.jongsoft.lang.control.CheckedSupplier;
import com.jongsoft.lang.control.Optional;
import com.jongsoft.lang.control.Try;
import com.jongsoft.lang.control.impl.None;
import com.jongsoft.lang.control.impl.Some;
import com.jongsoft.lang.control.impl.TryFailure;
import com.jongsoft.lang.control.impl.TrySuccess;

/**
 * The API class allows access to the libraries interfaces and control structures.
 */
public final class API {
    private static final Array<?> EMPTY_LIST = new Array<>(new Object[0]);
    private static final HashSet EMPTY_SET = new HashSet(new Object[0]);

    @SuppressWarnings("unchecked")
    private static final SortedSet EMPTY_SORTED_SET = new SortedSet(new Object[0], new EqualsComparator());
    @SuppressWarnings("unchecked")
    private static final HashMap<?, ?> EMPTY_MAP = new HashMap(EMPTY_LIST);

    private API() {
        // hidden constructor for utility class
    }

    /**
     * Creates a new {@link Optional} representing the provided value.
     * <p>
     * This method will allow <code>null</code> values.
     *
     * @param <T>       the type of the value
     * @param value     the actual value
     *
     * @return          an {@link Optional} representing the value
     */
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <T> Optional<T> Option(T value) {
        return value != null ? new Some<>(value) : (Optional<T>) None.INSTANCE;

    }

    /**
     * Attempt to execute code that will return an entity, but may also result into an exception.
     *
     * @param <T>      the type of entity that will be returned in case of success
     * @param supplier the supplier that will return the entity
     *
     * @return either an {@link Try} with success set to true and a get returning the entity, or a {@link Try} with
     *         failure set to true and the get throwing the exception.
     *
     * @throws NullPointerException in case the supplier is null
     */
    @SuppressWarnings("squid:S00100")
    public static <T> Try<T> Try(CheckedSupplier<T> supplier) {
        Objects.requireNonNull(supplier, "Supplier cannot be null");
        try {
            return new TrySuccess<>(supplier.get());
        } catch (Exception exception) {
            return new TryFailure<>(exception);
        }
    }

    /**
     * Attempt to execute the code in the {@link CheckedRunner}. If the execution results into an exception then this
     * call will return a {@link Try} with a failure result. Otherwise it will return an empty success {@link Try}.
     *
     * @param runner the code to execute with a try..catch construction
     *
     * @return A {@link Try} with success set to true if the execution went without exceptions, otherwise it will return
     *         a {@link Try} with a failure set to true.
     *
     * @throws NullPointerException in case the runner is null
     */
    @SuppressWarnings("squid:S00100")
    public static Try<Void> Try(CheckedRunner runner) {
        Objects.requireNonNull(runner, "Runner cannot be null");
        try {
            runner.run();
        } catch (Exception exception) {
            return new TryFailure<>(exception);
        }

        return new TrySuccess<>(null);
    }

    /**
     * Creates an {@link Sequence} containing exactly one element, being the one passed to this call.
     *
     * @param element   the element to add to the new array
     * @param <T>       the type of the element
     * @return          the new array list
     */
    @SuppressWarnings("squid:S00100")
    public static <T> Sequence<T> List(T element) {
        return create(new Object[]{element});
    }

    /**
     * Creates a new {@link Sequence} with the provided elements as the contents.
     *
     * @param elements  the elements to add to the array
     * @param <T>       the type that the elements represent
     * @return          the new array list
     *
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    @SuppressWarnings("squid:S00100")
    public static <T> Sequence<T> List(final T...elements) {
        Objects.requireNonNull(elements, "The provided elements cannot be null");
        return create(copyOf(elements, elements.length));
    }

    /**
     * Creates a new {@link Sequence} with all the elements contained in the {@link Iterable}.
     *
     * @param elements  the elements that should be in the new array
     * @param <T>       the type of the elements
     * @return          the new array
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
     * @param iterator  the iterator to copy into the array
     * @param <T>       the type of the elements
     * @return          the newly created array
     * @throws NullPointerException if {@code iterator} is null
     */
    @SuppressWarnings("squid:S00100")
    public static <T> Sequence<T> List(Iterator<T> iterator) {
        Objects.requireNonNull(iterator, "iterator is null");
        return create(iterator.toNativeArray());
    }

    /**
     * Create a new set containing all unique elements.
     *
     * @param elements  the elements for the new set
     * @param <T>       the type of the elements
     * @return  the created set
     */
    @SafeVarargs
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <T> Set<T> Set(T...elements) {
        Set<T> newSet = EMPTY_SET;

        for (int i = 0; i < elements.length; i++) {
            newSet = newSet.append(elements[i]);
        }

        return newSet;
    }

    /**
     * Create a new set containing all the elements of the {@code iterable}.
     *
     * @param iterable  the iterable containing the elements to add to the set
     * @param <T>       the type of the elements
     * @return  the created set
     * @throws NullPointerException if {@code iterable} is null
     */
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <T> Set<T> Set(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (iterable instanceof HashSet) {
            return (HashSet<T>) iterable;
        }

        return new HashSet<>(Iterator.of(iterable).toNativeArray());
    }

    /**
     * Create a new sorted set with all the {@code elements} in it sorted by the provided {@code comparator}.
     *
     * @param comparator    the comparator to use for sorting the set
     * @param elements      the elements initially in the set
     * @param <T>           the type of the elements
     * @return              the newly generated sorted set
     */
    @SafeVarargs
    @SuppressWarnings("squid:S00100")
    public static <T> Set<T> Set(Comparator<T> comparator, T...elements) {
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
    @SuppressWarnings("squid:S00100")
    public static <T> Set<T> Set(Comparator<T> comparator, Iterable<? extends T> iterable) {
        return new SortedSet<>(Iterator.of(iterable).toNativeArray(), comparator);
    }

    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <T> Set<T> SortedSet() {
        return EMPTY_SORTED_SET;
    }

    /**
     * Create a new empty {@link Map}.
     *
     * @param <K>   the type of the key
     * @param <T>   the type of the balue
     * @return      the new empty map
     */
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <K, T> Map<K, T> Map() {
        return (Map<K, T>) EMPTY_MAP;
    }

    /**
     * Create a new map with one pair in it being the provided {@code key}, {@code value}.
     *
     * @param key       the key use for the first element
     * @param value     the value use for the first element
     * @param <K>       the type of the key
     * @param <T>       the type of the balue
     * @return          the new map
     */
    @SuppressWarnings({"unchecked", "squid:S00100"})
    public static <K, T> Map<K, T> Map(K key, T value) {
        return (Map<K, T>) Map().put(key, value);
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
