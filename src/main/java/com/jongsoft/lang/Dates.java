package com.jongsoft.lang;

import com.jongsoft.lang.time.Range;
import com.jongsoft.lang.time.impl.RangeImpl;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class Dates {
    private Dates() {
        // left blank
    }

    /**
     * Creates a temporal range, where the range is valid from (inclusive) until (exclusive) a preset
     * temporal instance.
     *
     * @param from      the from (inclusive) temporal
     * @param until     the until (exclusive) temporal
     * @param <T>       the type of temporal
     * @return          the ranged temporal
     */
    public static <T extends Temporal> Range<T> range(T from, T until) {
        return new RangeImpl<>(from, until);
    }

    /**
     * Create a temporal range, starting from the provided {@code from} and with a size containing exactly one of the
     * provided chrono unit.
     *
     * @since 1.1.1
     * @param from      the start of the range (inclusive)
     * @param range     the size of the range
     * @param <T>       the type of the range
     * @return          the created range
     */
    public static <T extends Temporal> Range<T> range(T from, ChronoUnit range) {
        return new RangeImpl<>(from, range);
    }

}
