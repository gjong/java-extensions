package com.jongsoft.lang.time;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.util.stream.Stream;

/**
 * A temporal range to contain a from until temporal entities. Where the from is inclusive and the until is exclusive.
 *
 * @param <T> the temporal type
 */
public interface Range<T extends Temporal> {

    /**
     * The from part for this temporal range.
     *
     * @return the inclusive from part
     */
    T from();

    /**
     * Get a part of the from temporal of this range.
     *
     * @param part  the temporal field part to get
     * @return      the temporal field value
     */
    default int fromPart(TemporalField part) {
        return from().get(part);
    }

    /**
     * The until part for this temporal range.
     *
     * @return the exclusive until part
     */
    T until();

    /**
     * Get a part of the until temporal of this range.
     *
     * @param part  the temporal field part to get
     * @return      the temporal field value
     */
    default int untilPart(TemporalField part) {
        return until().get(part);
    }

    /**
     * Slice the temporal range into smaller sections, where each slice is at least the indicated ChronoUnit in size.
     *
     * @param slicing   the slicing range
     * @return          the sliced ranges
     */
    Stream<Range<T>> slice(ChronoUnit slicing);

    /**
     * Returns the range exactly before this range. If the range was created using a ChronoUnit then the window will shift
     * exactly one ChronoUnit back. If it was created using a from and until it will shift exactly the same size back.
     *
     * @return the previous window
     */
    Range<T> previous();

    Range<T> next();

}
