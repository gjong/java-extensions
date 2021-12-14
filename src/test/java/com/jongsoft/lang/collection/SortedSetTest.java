package com.jongsoft.lang.collection;

import com.jongsoft.lang.Collections;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SortedSetTest {

    @Test
    void add() {
        Set<Integer> sortedInts = Collections.Set(Integer::compareTo, 1, 3, 6)
                .append(2)
                .append(5)
                .append(2)
                .append(4);

        assertThat(sortedInts)
                .hasSize(6)
                .containsExactly(1, 2, 3, 4, 5, 6);
    }

    @Test
    void map() {
        Set<Integer> ints = Collections.Set(Integer::compareTo, 1, 2, 3)
                .map(x -> x * 2);

        assertThat(ints)
                .hasSize(3)
                .containsExactly(2, 4, 6);
    }

    @Test
    void remove() {
        Set<Integer> sortedInts = Collections.Set(Integer::compareTo, 1, 3, 6)
                .remove(1);

        assertThat(sortedInts)
                .hasSize(2)
                .containsExactly(1, 6);
    }

}
