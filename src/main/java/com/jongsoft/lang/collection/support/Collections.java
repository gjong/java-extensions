package com.jongsoft.lang.collection.support;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import com.jongsoft.lang.collection.ImmutableHashMap;
import com.jongsoft.lang.collection.Map;
import com.jongsoft.lang.collection.Sequence;
import com.jongsoft.lang.control.Control;

public final class Collections {

    public static <K, U> Map<K, Sequence<U>> groupBy(Supplier<Sequence<U>> instanceSupplier, Sequence<U> source,
            Function<? super U, ? extends K> keyGenerator) {
        Objects.requireNonNull(keyGenerator, "keyGenerator is null");
        Map<K, Sequence<U>> result = ImmutableHashMap.create();
        for (U element : source) {
            K key = keyGenerator.apply(element);
            Sequence<U> elementsForKey = Control.Option(result.get(key))
                    .getOrSupply(instanceSupplier);

            result = result.put(key, elementsForKey.append(element));
        }

        return result;
    }

}
