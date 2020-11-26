package com.jongsoft.lang.time;

import com.jongsoft.lang.Dates;
import com.jongsoft.lang.collection.Collectors;
import com.jongsoft.lang.collection.List;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

public class RangeTest {

    @Test
    public void slice_exact_2_months() {
        Range<LocalDate> range = Dates.range(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 3, 1));

        List<Range<LocalDate>> slices = range.slice(ChronoUnit.MONTHS)
                .collect(Collectors.toList());

        Assert.assertThat(slices.size(), CoreMatchers.is(2));
        Assert.assertThat(slices.get(0).from(), CoreMatchers.equalTo(LocalDate.of(2019, 1, 1)));
        Assert.assertThat(slices.get(0).until(), CoreMatchers.equalTo(LocalDate.of(2019, 2, 1)));
        Assert.assertThat(slices.get(1).from(), CoreMatchers.equalTo(LocalDate.of(2019, 2, 1)));
        Assert.assertThat(slices.get(1).until(), CoreMatchers.equalTo(LocalDate.of(2019, 3, 1)));
    }

    @Test
    public void slice_inexact_2_half_months() {
        Range<LocalDate> range = Dates.range(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 3, 15));

        List<Range<LocalDate>> slices = range.slice(ChronoUnit.MONTHS)
                .collect(Collectors.toList());

        Assert.assertThat(slices.size(), CoreMatchers.is(3));
        Assert.assertThat(slices.get(0).from(), CoreMatchers.equalTo(LocalDate.of(2019, 1, 1)));
        Assert.assertThat(slices.get(0).until(), CoreMatchers.equalTo(LocalDate.of(2019, 2, 1)));
        Assert.assertThat(slices.get(1).from(), CoreMatchers.equalTo(LocalDate.of(2019, 2, 1)));
        Assert.assertThat(slices.get(1).until(), CoreMatchers.equalTo(LocalDate.of(2019, 3, 1)));
        Assert.assertThat(slices.get(2).from(), CoreMatchers.equalTo(LocalDate.of(2019, 3, 1)));
        Assert.assertThat(slices.get(2).until(), CoreMatchers.equalTo(LocalDate.of(2019, 3, 15)));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void slice_exact_minutes_unsupported() {
        Range<LocalDate> range = Dates.range(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 1, 2));

        range.slice(ChronoUnit.MINUTES);
    }

    @Test
    public void previous_from_until() {
        Range<LocalDate> range = Dates.range(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 2, 1));

        Range<LocalDate> previous = range.previous();

        Assert.assertEquals(LocalDate.of(2018, 12, 1), previous.from());
        Assert.assertEquals(LocalDate.of(2019, 1, 1), previous.until());
    }

    @Test
    public void previous_chronounit() {
        Range<LocalDate> range = Dates.range(LocalDate.of(2019, 2, 1), ChronoUnit.DECADES);

        Range<LocalDate> previous = range.previous();

        Assert.assertEquals(LocalDate.of(2009, 2, 1), previous.from());
        Assert.assertEquals(LocalDate.of(2019, 2, 1), previous.until());
    }

}
