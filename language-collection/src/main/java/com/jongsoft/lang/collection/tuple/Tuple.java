package com.jongsoft.lang.collection.tuple;

import com.jongsoft.lang.collection.List;

public interface Tuple {

    interface Pair<X, Y> extends Tuple {
        X getFirst();
        Y getSecond();

        static <X, Y> Pair<X, Y> of(X left, Y right) {
            return new PairImpl<>(left, right);
        }
    }

    interface Triplet<X, Y, Z> extends Pair<X, Y> {
        Z getThird();

        static <X, Y, Z> Triplet<X, Y, Z> of(X first, Y second, Z third) {
            return new TripletImpl<>(first, second, third);
        }
    }

    interface Quadruplet<X, Y, Z, D> extends Triplet<X, Y, Z> {
        D getFourth();

        static <X, Y, Z, D> Quadruplet<X, Y, Z, D> of(X first, Y second, Z third, D fourth) {
            return new QuadrupletImpl<>(first, second, third, fourth);
        }
    }

    /**
     * Build an immutable {@link List} of all elements present in the {@link Tuple}.
     *
     * @return  the immutable {@link List} of the elements
     */
    List toList();

}
