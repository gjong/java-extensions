package com.jongsoft.lang.collection.tuple;

class PairImpl<X, Y> extends AbstractTuple implements Tuple.Pair<X, Y> {

    PairImpl(Object...elements) {
        super(elements);
    }

    @Override
    public X getFirst() {
        return (X) super.element(0);
    }

    @Override
    public Y getSecond() {
        return (Y) super.element(1);
    }
}
