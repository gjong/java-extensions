package com.jongsoft.lang.collection.tuple;

public class QuadrupletImpl<X, Y, Z, D> extends TripletImpl<X, Y, Z> implements Tuple.Quadruplet<X, Y, Z, D> {

    public QuadrupletImpl(Object... elements) {
        super(elements);
    }

    @Override
    public D getFourth() {
        return (D) super.element(3);
    }

}
