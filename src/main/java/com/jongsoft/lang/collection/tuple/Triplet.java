package com.jongsoft.lang.collection.tuple;

import com.jongsoft.lang.collection.tuple.impl.TripletImpl;

public interface Triplet<X, Y, Z> extends Pair<X, Y> {
    Z getThird();

    static <X, Y, Z> Triplet<X, Y, Z> of(X first, Y second, Z third) {
        return new TripletImpl<>(first, second, third);
    }

}
