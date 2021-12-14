package com.jongsoft.lang.time;

import com.jongsoft.lang.Dates;
import com.jongsoft.lang.collection.Collectors;
import com.jongsoft.lang.collection.List;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RangeTest {

    @Test
    void slice_exact_2_months() {
        Range<LocalDate> range = Dates.range(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 3, 1));

        List<Range<LocalDate>> slices = range.slice(ChronoUnit.MONTHS)
                .collect(Collectors.toList());

        assertThat(slices)
                .hasSize(2)
                .anySatisfy(slice -> {
                    assertThat(slice.from()).isEqualTo(LocalDate.of(2019, 1, 1));
                    assertThat(slice.until()).isEqualTo(LocalDate.of(2019, 2, 1));
                })
                .anySatisfy(slice -> {
                    assertThat(slice.from()).isEqualTo(LocalDate.of(2019, 2, 1));
                    assertThat(slice.until()).isEqualTo(LocalDate.of(2019, 3, 1));
                });
    }

    @Test
    void slice_inexact_2_half_months() {
        Range<LocalDate> range = Dates.range(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 3, 15));

        List<Range<LocalDate>> slices = range.slice(ChronoUnit.MONTHS)
                .collect(Collectors.toList());

        assertThat(slices)
                .hasSize(3)
                .anySatisfy(slice -> {
                    assertThat(slice.from()).isEqualTo(LocalDate.of(2019, 1, 1));
                    assertThat(slice.until()).isEqualTo(LocalDate.of(2019, 2, 1));
                })
                .anySatisfy(slice -> {
                    assertThat(slice.from()).isEqualTo(LocalDate.of(2019, 2, 1));
                    assertThat(slice.until()).isEqualTo(LocalDate.of(2019, 3, 1));
                })
                .anySatisfy(slice -> {
                    assertThat(slice.from()).isEqualTo(LocalDate.of(2019, 3, 1));
                    assertThat(slice.until()).isEqualTo(LocalDate.of(2019, 3, 15));
                });
    }

    @Test
    void slice_exact_minutes_unsupported() {
        Range<LocalDate> range = Dates.range(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 1, 2));

        assertThatThrownBy(() -> range.slice(ChronoUnit.MINUTES))
                .isInstanceOf(UnsupportedTemporalTypeException.class);
    }

    @Test
    void previous_from_until() {
        Range<LocalDate> range = Dates.range(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 2, 1));

        Range<LocalDate> previous = range.previous();

        assertThat(previous.from()).isEqualTo(LocalDate.of(2018, 12, 1));
        assertThat(previous.until()).isEqualTo(LocalDate.of(2019, 1, 1));
    }

    @Test
    void previous_chronounit() {
        Range<LocalDate> range = Dates.range(LocalDate.of(2019, 2, 1), ChronoUnit.DECADES);

        Range<LocalDate> previous = range.previous();

        assertThat(previous.from()).isEqualTo(LocalDate.of(2009, 2, 1));
        assertThat(previous.until()).isEqualTo(LocalDate.of(2019, 2, 1));
    }

    @Test
    void range_equals() {
        Range<LocalDate> range = Dates.range(LocalDate.of(2019, 1, 1), LocalDate.of(2019, 2, 1));

        assertThat(range)
                .isEqualTo(Dates.range(LocalDate.of(2019, 1, 1), ChronoUnit.MONTHS));
    }

}
