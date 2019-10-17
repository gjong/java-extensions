package com.jongsoft.lang.control;

/**
 * A control class to build an equality.
 */
public interface Equal {

    /**
     * Compare left with right for equality. Returns the result of the comparison and the previous comparison pipeline.
     *
     * @param left  the left element for comparison
     * @param right the right element for comparison
     * @param <T>   the type of the left element
     * @param <R>   the type of the right element
     * @return the Equal instance representing the equality of the pipeline
     */
    <T, R> Equal append(T left, R right);

    /**
     * Indicates if the result of the entire equality pipeline was the same.
     *
     * @return true if all {@link #append(Object, Object)} were equal.
     */
    boolean isEqual();

    /**
     * Indicates if the result of at least one equality in  the pipeline was not the same.
     *
     * @return true if at least one {@link #append(Object, Object)} was not equal.
     */
    boolean isNotEqual();

}
