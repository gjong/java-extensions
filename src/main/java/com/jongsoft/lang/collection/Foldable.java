/*
 * The MIT License
 *
 * Copyright 2016-2018 Jong Soft.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.jongsoft.lang.collection;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * <p>
 *   In functional programming, fold (also termed reduce, accumulate, aggregate, compress, or inject) refers to a family of higher-order
 *   functions that analyze a recursive data structure and through use of a given combining operation, recombine the results of recursively
 *   processing its constituent parts, building up a return value.
 * </p>
 * <p>
 *   This interface indicates that the implementation supports the folding operations.
 * </p>
 *
 * @param <T>   the type of the component
 * @see <a href="https://en.wikipedia.org/wiki/Fold_(higher-order_function)">Fold operation explained</a>
 * @since 0.0.3
 */
public interface Foldable<T> {

    /**
     * Folds the elements using the given binary operation {@code combiner}, starting with the {@code start} and successively calling the
     * {@code combiner}.
     *
     * @param start      the start value for the fold operation
     * @param combiner   the operation used to combine elements
     * @return           the folded value
     *
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
     * <p>
     *   <strong>Example:</strong>
     * </p>
     * <pre>{@code // result "!test"
     *    List("t", "e", "s", "t").foldLeft("!",  (x, y) -> x + y)
     * }</pre>
     *
     * @param start      the start value for the fold operation
     * @param combiner   the operation used to combine elements
     * @param <U>        the type of value returned
     * @return           the folded value
     *
     * @throws NullPointerException if {@code combiner} is null
     */
    <U> U foldLeft(U start, BiFunction<? super U, ? super T, ? extends U> combiner);

    /**
     * Folds the elements from the right, starting with the {@code start} value and combining the result by successively calling the
     * {@code combiner}.
     * <p>
     *   <strong>Example:</strong>
     * </p>
     * <pre>{@code // result "tset!"
     *    List("t", "e", "s", "t").foldRight("!",  (x, y) -> x + y)
     * }</pre>
     *
     * @param start      the start value for the fold operation
     * @param combiner   the operation used to combine elements
     * @param <U>        the targeted type of combination method
     * @return           the folded value
     *
     * @throws NullPointerException if {@code combiner} is null
     */
    <U> U foldRight(U start, BiFunction<? super T, ? super U, ? extends U> combiner);

    /**
     * Convenience method for {@code reduceLeft}.
     *
     * @param reducer   the operation to use to reduce the elements to one value
     * @return          the reduced value
     * @see #reduceLeft(BiFunction)
     */
    default T reduce(BiFunction<? super T, ? super T, ? extends T> reducer) {
        return reduceLeft(reducer);
    }

    /**
     * <p>
     *   Reduce the collection from the left, starting with the first element in the collection and successively calling the {@code reducer
     *   } until there are no more elements.
     * </p>
     * <p>
     *   <strong>Example:</strong>
     * </p>
     * <pre>{@code // result "test"
     *    List("t", "e", "s", "t").reduceLeft((x, y) -> x + y)
     * }</pre>
     *
     * <p>
     *     This operation is similar to the {@linkplain #foldLeft(Object, BiFunction)} where the first element is the head of the
     *     elements.
     * </p>
     *
     * @param reducer   the operation to use to reduce the elements to one value
     * @return          the reduced value
     *
     * @see #foldLeft(Object, BiFunction)
     * @throws NullPointerException if {@code reducer} is null
     */
    T reduceLeft(BiFunction<? super T, ? super T, ? extends T> reducer);

}
