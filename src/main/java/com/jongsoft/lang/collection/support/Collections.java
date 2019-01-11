package com.jongsoft.lang.collection.support;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.Collection;
import com.jongsoft.lang.collection.Map;
import com.jongsoft.lang.collection.Sequence;

public final class Collections {

    private Collections() {
        // hidden constructor utility class
    }

    public static <K, U> Map<K, Sequence<U>> groupBy(Supplier<Sequence<U>> instanceSupplier, Sequence<U> source,
                                                     Function<? super U, ? extends K> keyGenerator) {
        Objects.requireNonNull(keyGenerator, "keyGenerator is null");
        Map<K, Sequence<U>> result = API.Map();
        for (U element : source) {
            K key = keyGenerator.apply(element);
            Sequence<U> elementsForKey = API.Option(result.get(key))
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

    public static <T> String textValueOf(String type, Collection<T> collection) {
        StringBuilder text = new StringBuilder(type)
                .append("[");

        int index = 0;
        for (T element : collection) {
            text.append(element);

            index++;
            if (index < collection.size()) {
                text.append(", ");
            }
        }
        text.append("]");

        return text.toString();
    }

}
