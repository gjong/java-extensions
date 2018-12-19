package com.jongsoft.lang.collection;

import java.util.Objects;
import java.util.function.Predicate;

/**
 *
 * @param <T>   the entity type of the sequence
 * @since 0.0.5
 */
public interface List<T> extends Collection<T> {

    /**
     * Create a new sequence starting with all elements in this sequence and ending with the provided {@code value}.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code  // will result in a list with 2, 3, 4, 1
     *    List(2, 3, 4).append(1)
     * }</pre>
     *
     * @param value     the value to append to the list
     * @return          the new list with the value appended
     */
    List<T> append(T value);

    /**
     * Removes an element from the list and returns a new instance of the list.
     *
     * @param index     the index of the element to be removed
     * @return          the new instance of the list without the element at the provided index
     * @throws IndexOutOfBoundsException    in case the index is not between the 0 and list size
     */
    List<T> remove(int index);

    /**
     * Get the element at the location of <code>index</code>
     *
     * @param index     the index of the element in the list to get
     * @return          the element at the provided index
     * @throws IndexOutOfBoundsException    in case the index provided is greater then the {@link #size()} - 1.
     */
    T get(int index);

    /**
     * Find the index for the provided element, will return <code>-1</code> if the element
     * is not present in the list.
     *
     * @param lookFor   the element to look for
     * @return  the index of the element, or <code>-1</code> if none found
     */
    default int indexOf(Object lookFor) {
        return firstIndexWhere(e -> Objects.equals(lookFor, e));
    }

    /**
     * Search the collections for the first element matching the provided {@link Predicate} and return the index
     * position of that element.
     *
     * @param predicate the predicate to match
     * @return  index of the found element, -1 if none found
     */
    int firstIndexWhere(Predicate<T> predicate);
}
