package com.jongsoft.lang.collection.tuple;

import com.jongsoft.lang.collection.tuple.impl.QuadrupletImpl;

public interface Quadruplet<X, Y, Z, D> extends Triplet<X, Y, Z> {
    D getFourth();

    static <X, Y, Z, D> Quadruplet<X, Y, Z, D> of(X first, Y second, Z third, D fourth) {
        return new QuadrupletImpl<>(first, second, third, fourth);
    }
}
