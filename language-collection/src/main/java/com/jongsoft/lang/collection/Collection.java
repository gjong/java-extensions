package com.jongsoft.lang.collection;

import java.util.function.Function;
import java.util.function.Predicate;

import com.jongsoft.lang.common.core.Value;

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

    @Override
    <U> Collection<U> map(Function<T, U> mapper);

    @Override
    Iterator<T> iterator();
}
