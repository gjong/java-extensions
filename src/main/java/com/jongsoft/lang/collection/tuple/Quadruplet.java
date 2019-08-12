package com.jongsoft.lang.collection.tuple;

public interface Quadruplet<X, Y, Z, D> extends Triplet<X, Y, Z> {

    /**
     * Get the fourth element from the tuple.
     *
     * @return the element
     */
    D getFourth();

}
