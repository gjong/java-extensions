package com.jongsoft.lang.collection.tuple;

public interface Triplet<X, Y, Z> extends Pair<X, Y> {

    /**
     * Get the third element from the tuple.
     *
     * @return the element
     */
    Z getThird();

}
