package com.jongsoft.lang.collection.tuple;

class TripletImpl<X, Y, Z> extends PairImpl<X, Y> implements Tuple.Triplet<X, Y, Z> {

    public TripletImpl(Object... elements) {
        super(elements);
    }

    @Override
    public Z getThird() {
        return (Z) super.element(2);
    }
}
