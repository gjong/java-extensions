/*
 * The MIT License
 *
 * Copyright 2016-2019 Jong Soft.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.jongsoft.lang.collection.tuple;

import com.jongsoft.lang.collection.Sequence;

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
     * Build an immutable {@link Sequence} of all elements present in the {@link Tuple}.
     *
     * @return  the immutable {@link Sequence} of the elements
     */
    Sequence toList();

}
