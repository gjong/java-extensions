package com.jongsoft.lang.time.impl;

import com.jongsoft.lang.time.Range;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RangeImpl<T extends Temporal> implements Range<T> {

    private final ChronoUnit preset;
    private final T from;
    private final T until;

    public RangeImpl(T from, T until) {
        this.from = from;
        this.until = until;
        this.preset = null;
    }

    @SuppressWarnings("unchecked")
    public RangeImpl(T from, ChronoUnit range) {
        this.from = from;
        this.until = (T) from.plus(1, range);
        this.preset = range;
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

    @Override
    public Range<T> previous() {
        return shift(-1);
    }

    @Override
    public Range<T> next() {
        return shift(1);
    }

    @SuppressWarnings("unchecked")
    private Range<T> shift(int multiplier) {
        if (preset != null) {
            return new RangeImpl<>(
                    (T) from.plus(multiplier, preset),
                    preset
            );
        }

        ChronoUnit measurement = from.isSupported(ChronoUnit.MINUTES)
                ? ChronoUnit.MINUTES
                : ChronoUnit.DAYS;

        long windowSize = measurement.between(from, until);
        return new RangeImpl<>(
                (T) from.plus(windowSize * multiplier, measurement),
                (T) until.plus(windowSize * multiplier, measurement)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Range) {
            Range<?> other = (Range<?>) o;

            return Objects.equals(from, other.from())
                    && Objects.equals(until, other.until());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(17, from, until);
    }
}
