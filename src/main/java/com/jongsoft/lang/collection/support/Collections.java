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
package com.jongsoft.lang.collection.support;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.Collection;
import com.jongsoft.lang.collection.List;
import com.jongsoft.lang.collection.Map;

public final class Collections {

    private Collections() {
        // hidden constructor utility class
    }

    public static <K, U> Map<K, List<U>> groupBy(Supplier<List<U>> instanceSupplier, List<U> source,
                                                     Function<? super U, ? extends K> keyGenerator) {
        Objects.requireNonNull(keyGenerator, "keyGenerator is null");
        Map<K, List<U>> result = API.Map();
        for (U element : source) {
            K key = keyGenerator.apply(element);
            List<U> elementsForKey = API.Option(result.get(key))
                                            .getOrSupply(instanceSupplier);

            result = result.put(key, elementsForKey.append(element));
        }

        return result;
    }

    public static <T, K extends Collection<T>> Collector<T, ArrayList<T>, K> collector(Function<Iterable<? extends T>, K> finisher) {
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };

        return Collector.of(ArrayList::new, ArrayList::add, combiner, finisher::apply);
    }

    @SuppressWarnings("unchecked")
    public static <T, K extends List<T>> K filter(K seed, Iterable<T> source, Predicate<T> filter) {
        K result = seed;

        for (T element : source) {
            if (filter.test(element)) {
                result = (K) result.append(element);
            }
        }

        return result;
    }

    public static <T> String textValueOf(String type, Collection<T> collection) {
        StringBuilder text = new StringBuilder(type);

        text.append("[");
        if (collection != null) {
            int index = 0;
            for (T element : collection) {
                text.append(element);

                index++;
                if (index < collection.size()) {
                    text.append(", ");
                }
            }
        }
        text.append("]");

        return text.toString();
    }

}
