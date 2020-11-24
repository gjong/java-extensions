package com.jongsoft.lang.time.impl;

import com.jongsoft.lang.time.Range;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RangeImpl<T extends Temporal> implements Range<T> {

    private final T from;
    private final T until;

    public RangeImpl(T from, T until) {
        this.from = from;
        this.until = until;
    }

    @Override
    public T from() {
        return from;
    }

    @Override
    public T until() {
        return until;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stream<Range<T>> slice(ChronoUnit slicing) {
        long slices = slicing.between(from, until);

        Stream<Range<T>> streamSlices = IntStream.range(0, (int) slices)
                .mapToObj(slice -> (T) from.plus(slice, slicing))
                .map(start -> new RangeImpl<>(start, (T) start.plus(1, slicing)));

        Stream<Range<T>> addonSlice = Stream.empty();
        T slicedEnd = (T) from.plus(slices, slicing);
        if (!slicedEnd.equals(until)) {
            addonSlice = Stream.of(new RangeImpl<>(slicedEnd, until));
        }

        return Stream.concat(streamSlices, addonSlice);
    }

}
