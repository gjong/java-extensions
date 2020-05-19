package com.jongsoft.lang.collection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collector;

import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.impl.TailedList;
import com.jongsoft.lang.collection.support.Collections;

public class Collectors {

    private Collectors() {
        // hidden utility constructor
    }

    public static <T> Collector<T, ArrayList<T>, List<T>> toList() {
        return Collections.collector(API::List);
    }

    public static <T> Collector<T, ArrayList<T>, List<T>> toLinkedList() {
        return Collections.collector(TailedList::ofAll);
    }

    public static <T> Collector<T, ArrayList<T>, Set<T>> toSet() {
        return Collections.collector(API::Set);
    }

    public static <T> Collector<T, ArrayList<T>, Set<T>> toSorted(Comparator<T> comparator) {
        return Collections.collector(x -> API.Set(comparator, x));
    }

}
