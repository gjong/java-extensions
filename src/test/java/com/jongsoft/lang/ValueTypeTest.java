package com.jongsoft.lang;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueTypeTest {

    @Test
    void isSingleValued() {
        assertThat(new ValueType<>("test").isSingleValued()).isTrue();
    }

    @Test
    void get() {
        assertThat(new ValueType<>(1).get()).isEqualTo(1);
    }

    @Test
    void filter_matching() {
        String result = new ValueType<>("test")
                .filter(str -> str.length() > 1)
                .get();

        assertThat(result).isEqualTo("test");
    }

    @Test
    void filter_not_matching() {
        assertThatThrownBy(() ->
                new ValueType<>("test")
                        .filter(str -> str.length() > 10)
                        .get())
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Cannot get head on empty collection");
    }

    @Test
    void map() {
        int length = new ValueType<>("test")
                .map(String::length)
                .get();

        assertThat(length).isEqualTo(4);
    }

    @Test
    void iterator() {
        String value = new ValueType<>("test")
                .iterator()
                .next();

        assertThat(value).isEqualTo("test");
    }
}
