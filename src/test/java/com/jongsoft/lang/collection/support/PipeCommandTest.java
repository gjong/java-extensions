package com.jongsoft.lang.collection.support;

import com.jongsoft.lang.Collections;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class PipeCommandTest {

    @Test
    void consume() {
        StringBuilder consumed = new StringBuilder();

        new PipeCommand<>(Collections.List("text 1", "car", "plane"))
                .map(String::length)
                .consume(consumed::append);

        assertThat(consumed).hasToString("635");
    }

    @Test
    void pipeline() {
        java.util.List<String> strings = Collections.List("car", "plane", "bike")
           .pipeline()
           .reject(s -> s.length() > 4)
           .stream().collect(Collectors.toList());

        assertThat(strings)
                .hasSize(2)
                .containsExactly("car", "bike");
    }

    @Test
    void foldLeft() {
        assertThat(Collections.List("t", "e", "s", "t").pipeline().foldLeft("!", (x, y) -> x + y))
                .isEqualTo("!test");
    }

    @Test
    void foldRight() {
        assertThat(Collections.List("t", "e", "s", "t").pipeline().foldRight("!", (x, y) -> x + y))
                .isEqualTo("tset!");
    }

    @Test
    void reduceLeft() {
        assertThat(Collections.List("t", "e", "s", "t").pipeline().reduceLeft((x ,y) -> x + y))
                .isEqualTo("test");
    }

}
