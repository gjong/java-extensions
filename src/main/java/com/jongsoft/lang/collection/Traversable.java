package com.jongsoft.lang.collection;

import java.util.function.Function;
import java.util.function.Predicate;

import com.jongsoft.lang.Value;
import com.jongsoft.lang.control.Optional;

public interface Traversable<T> extends Value<T>, Foldable<T> {

    /**
     * Return a list that removes all elements that match the {@code predicate} provided.
     *
     * @param predicate the predicate to use
     * @return          the elements that do not match the {@code predicate}
     * @throws NullPointerException in case {@code predicate} is null
     */
    Traversable<T> reject(Predicate<T> predicate);

    Traversable<T> filter(Predicate<T> predicate);

    <U> Traversable<U> map(Function<T, U> mapper);

    @Override
    Iterator<T> iterator();

    /**
     * Find the first match in the elements using the provided {@link Predicate}.
     * The returned {@linkplain Optional} is {@code null} safe and will either contain the element or be an empty {@link Optional}.
     * <p></p>
     * <p><strong>Example:</strong></p>
     * <pre>{@code // the result will be an Optional with the value 2
     *    int firstMatch = Collection(1, 2, 3, 4)
     *          .first(i -> i % 2 == 0);}</pre>
     *
     * @param predicate the predicate to use
     * @return          the first match found
     * @throws NullPointerException in case that the predicate is null
     */
    Optional<T> first(Predicate<T> predicate);

    /**
     * Find the last match in the Iterator using the provided {@link Predicate}.
     * The returned {@linkplain Optional} is {@code null} safe and will either contain the element or be an empty {@link Optional}.
     * <p></p>
     * <p><strong>Example:</strong></p>
     * <pre>{@code // the result will be an Optional with the value 4
     *    int firstMatch = Collection(1, 2, 3, 4)
     *          .last(i -> i % 2 == 0);}</pre>
     *
     * @param predicate the predicate to use
     * @return          the last match found
     * @throws NullPointerException in case that the predicate is null
     */
    Optional<T> last(Predicate<T> predicate);

}
