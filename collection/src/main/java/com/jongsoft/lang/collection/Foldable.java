package com.jongsoft.lang.collection;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Interface for foldable entities.
 *
 * @param <T>   the type of the component
 * @see <a href="https://en.wikipedia.org/wiki/Fold_(higher-order_function)">Fold operation explained</a>
 */
public interface Foldable<T> {

    /**
     * Folds the elements using the given binary operation {@code combiner}, starting with the {@code start} and successively calling the
     * {@code combiner}.
     *
     * @param start      the start value for the fold operation
     * @param combiner   the operation used to combine elements
     * @return           the folded value
     * @see #foldLeft(Object, BiFunction)
     * @throws NullPointerException if {@code combiner} is null
     */
    default T fold(T start, BiFunction<? super T, ? super T, ? extends T> combiner) {
        Objects.requireNonNull(combiner, "combiner cannot be null");
        return foldLeft(start, combiner);
    }

    /**
     * Folds the elements from the left, starting with the {@code start} value and combining the result by successively calling the
     * {@code combiner} operation.
     *
     * <pre>{@code
     *    // result "!test"
     *    Array("t", "e", "s", "t").foldLeft("!",  (x, y) -> x + y)
     * }</pre>
     *
     * @param start      the start value for the fold operation
     * @param combiner   the operation used to combine elements
     * @param <U>
     * @return           the folded value
     * @throws NullPointerException if {@code combiner} is null
     */
    <U> U foldLeft(U start, BiFunction<? super U, ? super T, ? extends U> combiner);

    /**
     * Folds the elements from the right, starting with the {@code start} value and combining the result by successively calling the
     * {@code combiner}.
     *
     * <pre>{@code
     *    // result "tset!"
     *    Array("t", "e", "s", "t").foldRight("!",  (x, y) -> x + y)
     * }</pre>
     *
     * @param start      the start value for the fold operation
     * @param combiner   the operation used to combine elements
     * @param <U>        the targeted type of combination method
     * @return           the folded value
     * @throws NullPointerException if {@code combiner} is null
     */
    <U> U foldRight(U start, BiFunction<? super T, ? super U, ? extends U> combiner);

}
