package com.jongsoft.lang.collection.support;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.jongsoft.lang.collection.Collection;
import com.jongsoft.lang.collection.HashMap;
import com.jongsoft.lang.collection.Map;
import com.jongsoft.lang.collection.Sequence;
import com.jongsoft.lang.control.Control;

public final class Collections {

    public static <K, U> Map<K, Sequence<U>> groupBy(Supplier<Sequence<U>> instanceSupplier, Sequence<U> source,
            Function<? super U, ? extends K> keyGenerator) {
        Objects.requireNonNull(keyGenerator, "keyGenerator is null");
        Map<K, Sequence<U>> result = HashMap.create();
        for (U element : source) {
            K key = keyGenerator.apply(element);
            Sequence<U> elementsForKey = Control.Option(result.get(key))
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

}
