package com.jongsoft.lang.collection.tuple;

import com.jongsoft.lang.collection.tuple.impl.PairImpl;

public interface Pair<X, Y> extends Tuple {

    X getFirst();

    Y getSecond();

    static <X, Y> Pair<X, Y> of(X left, Y right) {
        return new PairImpl<>(left, right);
    }
}
