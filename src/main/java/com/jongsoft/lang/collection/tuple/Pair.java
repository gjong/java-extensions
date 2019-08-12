package com.jongsoft.lang.collection.tuple;

public interface Pair<X, Y> extends Tuple {

    /**
     * Get the first element from the tuple.
     *
     * @return the element
     */
    X getFirst();

    /**
     * Get the second element from the tuple.
     *
     * @return the element
     */
    Y getSecond();

}
