package com.jongsoft.lang.collection;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.jongsoft.lang.API;
import com.jongsoft.lang.Control;
import com.jongsoft.lang.Value;
import com.jongsoft.lang.collection.support.Collections;
import com.jongsoft.lang.collection.tuple.Pair;
import com.jongsoft.lang.control.Optional;

public interface Traversable<T> extends Value<T>, Foldable<T> {

    Traversable<T> filter(Predicate<T> predicate);

    /**
     * Find the first match in the elements using the provided {@link Predicate}.
     * The returned {@linkplain Optional} is {@code null} safe and will either contain the element or be an empty {@link Optional}.
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

    @Override
    Iterator<T> iterator();

    /**
     * Find the last match in the Iterator using the provided {@link Predicate}.
     * The returned {@linkplain Optional} is {@code null} safe and will either contain the element or be an empty {@link Optional}.
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

    <U> Traversable<U> map(Function<T, U> mapper);

    /**
     * Returns either {@code this} if it is non empty, otherwise will return the provided {@code other}.
     *
     * @param other the alternative
     * @return this {@code Traversable} if non empty, or {@code other}
     */
    Traversable<T> orElse(Iterable<? extends T> other);

    /**
     * Returns either {@code this} if it is non empty, otherwise the provided supplier is evaluated and returned.
     *
     * @param supplier the supplier to generate the other
     * @return this (@code Traversable} if non empty, otherwise other
     */
    Traversable<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);

    /**
     * Return a list that removes all elements that match the {@code predicate} provided.
     *
     * @param predicate the predicate to use
     * @return          the elements that do not match the {@code predicate}
     * @throws NullPointerException in case {@code predicate} is null
     */
    Traversable<T> reject(Predicate<T> predicate);

    /**
     * Calculates the sum of this elements. Supported component types are {@code Byte}, {@code Double}, {@code Float},
     * {@code Integer}, {@code Long}, {@code Short}, {@code BigInteger} and {@code BigDecimal}.
     * <p>
     * Examples:
     * <pre>{@code
     * API.List().sum()                          // = Optional()
     * API.List(1, 2, 3).sum()                   // = Optional(6.0)
     * API.List(1.0, 10e100, 2.0, -10e100).sum() // = Optional(3.0)
     * API.List(1.0, Double.NaN).sum()           // = NaN
     * API.List("apple", "pear").sum()           // throws an exception
     * }</pre>
     *
     * @return {@code Optional(sum)}, or {@code Optional()} if no elements are present
     * @throws ClassCastException if the elements are not numeric
     */
    default Optional<Double> sum() {
        Pair<Integer, Double> summation = Collections.neumaierSum(this,  t -> ((Number) t).doubleValue());
        return summation.getFirst() == 0 ? Control.Option() : Control.Option(summation.getSecond());
    }

    /**
     Calculates the average of this elements, assuming that the element type is {@link Number}.
     *
     * Since we do not know if the component type {@code T} is of type {@code Number}, the
     * {@code average()} call might throw at runtime (see examples below).
     * <p>
     * Examples
     * <pre>{@code
     * API.List().average()                          // = Optional()
     * API.List(1, 2, 3).average()                   // = Optional(2.0)
     * API.List(1.0, 10e100, 2.0, -10e100).average() // = Optional(0.75)
     * API.List(1.0, Double.NaN).average()           // = NaN
     * API.List("apple", "pear").average()           // throws an exception
     * }</pre>
     *
     * @return {@code Optional(average), or {@code Optional()} if no elements are present}
     * @throws ClassCastException if the elements are not numeric
     */
    default Optional<Double> average() {
        Pair<Integer, Double> summation = Collections.neumaierSum(this,  t -> ((Number) t).doubleValue());
        return summation.getFirst() == 0
                ? Control.Option()
                : Control.Option(summation.getSecond() / summation.getFirst());
    }
}
