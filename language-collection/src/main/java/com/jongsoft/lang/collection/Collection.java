package com.jongsoft.lang.collection;

import java.util.function.Function;
import java.util.function.Predicate;

import com.jongsoft.lang.common.core.Value;
import com.jongsoft.lang.control.Optional;

public interface Collection<T> extends Iterable<T>, Value<T> {

    /**
     * Filter the list contents with the provided predicate. Only returning those elements that match the predicate.
     *
     * @param predicate the predicate to use in the filter operation
     * @return          the filtered list
     */
    Collection<T> filter(Predicate<T> predicate);

    /**
     * Get the amount of elements contained in the collection.
     *
     * @return 0 if empty, otherwise the amount of entries
     */
    int size();

    /**
     * Find the first match in the Iterator using the provided {@link Predicate}.
     *
     * @param predicate the predicate to use
     * @return          the first match found
     * @throws NullPointerException in case that the predicate is null
     */
    default Optional<T> findFirst(Predicate<T> predicate) {
        return iterator().findFirst(predicate);
    }

    /**
     * Find the last match in the Iterator using the provided {@link Predicate}.
     *
     * @param predicate the predicate to use
     * @return          the last match found
     * @throws NullPointerException in case that the predicate is null
     */
    default Optional<T> findLast(Predicate<T> predicate) {
        return iterator().findLast(predicate);
    }

    @Override
    <U> Collection<U> map(Function<T, U> mapper);

    @Override
    Iterator<T> iterator();
}
