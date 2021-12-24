package com.jongsoft.lang.collection;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CollectorsTest {

    @Test
    void toList() {
        List<String> result = Stream.of("one", "two", "three")
                .collect(Collectors.toList());

        assertThat(result)
                .hasSize(3)
                .containsExactly("one", "two", "three");
    }

    @Test
    void toSet() {
        Set<String> result = Stream.of("one", "two", "three", "two")
                .collect(Collectors.toSet());

        assertThat(result).hasSize(3)
                .containsExactly("one", "two", "three");
    }

    @Test
    void toSorted() {
        Set<String> result = Stream.of("one", "two", "three", "two")
                .collect(Collectors.toSorted(String::compareTo));

        assertThat(result).hasSize(3)
                        .containsExactly("one", "three", "two");
    }

}
