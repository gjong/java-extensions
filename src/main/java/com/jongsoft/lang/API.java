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
package com.jongsoft.lang;

import com.jongsoft.lang.collection.tuple.Pair;
import com.jongsoft.lang.collection.tuple.Quadruplet;
import com.jongsoft.lang.collection.tuple.Triplet;
import com.jongsoft.lang.collection.tuple.impl.PairImpl;
import com.jongsoft.lang.collection.tuple.impl.QuadrupletImpl;
import com.jongsoft.lang.collection.tuple.impl.TripletImpl;

/**
 * The API class allows access to the libraries interfaces and control structures.
 */
@SuppressWarnings("java:S100")
public final class API {

    private API() {
        // hidden constructor for utility class
    }

    /**
     * Create a new tuple containing 2 elements.
     *
     * @param first     the first element of the pair
     * @param second    the second element of the pair
     * @param <X>       the first element type
     * @param <Y>       the second element type
     * @return          the created tuple
     */
    public static <X, Y> Pair<X, Y> Tuple(X first, Y second) {
        return new PairImpl<>(first, second);
    }

    /**
     * Create a new tuple containing 3 elements.
     *
     * @param first     the first element of the tuple
     * @param second    the second element of the tuple
     * @param third     the third element of the tuple
     * @param <X>       the first element type
     * @param <Y>       the second element type
     * @param <Z>       the third element type
     * @return          the created tuple
     */
    public static <X, Y, Z> Triplet<X, Y, Z> Tuple(X first, Y second, Z third) {
        return new TripletImpl<>(first, second, third);
    }

    /**
     * Create a new tuple containing 4 elements.
     *
     * @param first     the first element of the tuple
     * @param second    the second element of the tuple
     * @param third     the third element of the tuple
     * @param fourth    the fourth element of the tuple
     * @param <X>       the first element type
     * @param <Y>       the second element type
     * @param <Z>       the third element type
     * @param <D>       the fourth element type
     * @return          the created tuple
     */
    public static <X, Y, Z, D> Quadruplet<X, Y, Z, D> Tuple(X first, Y second, Z third, D fourth) {
        return new QuadrupletImpl<>(first, second, third, fourth);
    }

    @SuppressWarnings("unchecked")
    public static <X, Y extends Pair<X, X>> Y Tuple(X...elements) {
        switch (elements.length) {
            case 2 : return (Y) API.Tuple(elements[0], elements[1]);
            case 3 : return (Y) API.Tuple(elements[0], elements[1], elements[2]);
            case 4 : return (Y) API.Tuple(elements[0], elements[1], elements[2], elements[3]);
            default: throw new IllegalArgumentException("Cannot construct a tuple of more then 4 elements.");
        }
    }

}
