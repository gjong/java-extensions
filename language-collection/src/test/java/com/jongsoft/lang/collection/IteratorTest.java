package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.jongsoft.lang.control.Optional;

public class IteratorTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void ofInteger() {
        final Iterator<Integer> numbers = Iterator.of(1, 2, 3, 4, 5);

        final Optional<Integer> firstMatch = numbers.first(i -> i % 2 == 0);
        final Optional<Integer> lastMatch = numbers.last(i -> i % 2 == 0);

        assertThat(firstMatch.isPresent(), equalTo(true));
        assertThat(firstMatch.get(), equalTo(2));
        assertThat(lastMatch.isPresent(), equalTo(true));
        assertThat(lastMatch.get(), equalTo(4));
    }

    @Test
    public void concatInteger() {
        final Iterator<Integer> numbers = Iterator.concat(Iterator.of(1), Iterator.of(2));

        assertThat(numbers.hasNext(), equalTo(true));
        assertThat(numbers.next(), equalTo(1));
        assertThat(numbers.hasNext(), equalTo(true));
        assertThat(numbers.next(), equalTo(2));
        assertThat(numbers.hasNext(), equalTo(false));
    }

    @Test
    public void empty() {
        thrown.expect(NoSuchElementException.class);

        final Iterator<Object> empty = Iterator.empty();

        assertThat(empty.hasNext(), equalTo(false));
        empty.next();
    }

    @Test
    public void get() {
        final Integer number = Iterator.of(1, 2, 3, 4, 5).get();
        assertThat(number, equalTo(1));
    }

    @Test
    public void map() {
        final Iterator<Integer> mapped = Iterator.of("one", "two", "three", "four")
                .map(String::length);

        assertThat(mapped.next(), equalTo(3));
        assertThat(mapped.next(), equalTo(3));
        assertThat(mapped.next(), equalTo(5));
        assertThat(mapped.next(), equalTo(4));
        assertThat(mapped.hasNext(), equalTo(false));
    }
}
