package com.jongsoft.lang.collection;

import com.jongsoft.lang.Streamable;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A pipeline is a set of commands that will be applied on top any {@link Collection}. Each command is lazy and will
 * only get executed once the pipe is terminated.
 * <p>
 *     Note that a pipeline can be split by re-using the intermediate operation. Each pipeline after the split will
 *     yield a different result and not modify the steps in between.
 * </p>
 * <p></p>
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 *    Pipeline<Integer> intermediatePipe = API.List(1, 2, 3, 4)
 *          .pipeline()
 *          .filter(x -> x % 2 == 0);
 *
 *    Pipeline<String> stringPipe = intermediatePipe.map(String::valueOf);
 *    Pipeline<Integer> finalPipe = intermediatePipe.map(x -> x * x / 3);
 *
 * }</pre>
 *
 * @param <T> the type of elements contained in the original collection
 * @since 1.0.6
 */
public interface Pipeline<T> extends Foldable<T>, Streamable<T> {

    @Override
    <U> Pipeline<U> map(Function<T, U> mapper);

    /**
     * Reject all values that match the provided predicate. This is the logical inverse of the operation
     * {@link #filter(Predicate)}.
     *
     * @param predicate the predicate to use
     * @return a pipeline instruction rejecting all values matching the predicate
     */
    Pipeline<T> reject(Predicate<T> predicate);

    @Override
    Pipeline<T> filter(Predicate<T> predicate);

    /**
     * This will return the iterator for this pipelines elements. This is a terminal operation.
     *
     * @return the iterator for all elements in the pipeline
     */
    @Override
    Iterator<T> iterator();
}
