package com.jongsoft.lang;

import com.jongsoft.lang.time.Range;
import com.jongsoft.lang.time.impl.RangeImpl;

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

}
