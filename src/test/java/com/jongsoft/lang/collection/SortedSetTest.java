package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SortedSetTest {

    @Test
    public void add() {
        Set<Integer> sortedInts = SortedSet.of(Integer::compareTo, 1, 3, 6)
                .add(2)
                .add(5)
                .add(2)
                .add(4);

        assertThat(sortedInts.size(), equalTo(6));
        assertThat(sortedInts.get(0), equalTo(1));
        assertThat(sortedInts.get(1), equalTo(2));
        assertThat(sortedInts.get(2), equalTo(3));
        assertThat(sortedInts.get(3), equalTo(4));
        assertThat(sortedInts.get(4), equalTo(5));
        assertThat(sortedInts.get(5), equalTo(6));
    }

    @Test
    public void map() {
        Set<Integer> ints = SortedSet.of(Integer::compareTo, 1, 2, 3)
                 .map(x -> x * 2);

        assertThat(ints.size(), equalTo(3));
        assertThat(ints.get(0), equalTo(2));
        assertThat(ints.get(1), equalTo(4));
        assertThat(ints.get(2), equalTo(6));
    }

    @Test
    public void remove() {
        Set<Integer> sortedInts = SortedSet.of(Integer::compareTo, 1, 3, 6)
                .remove(1);

        assertThat(sortedInts.size(), equalTo(2));
        assertThat(sortedInts.get(0), equalTo(1));
        assertThat(sortedInts.get(1), equalTo(6));
    }

}
