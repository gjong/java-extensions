package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import com.jongsoft.lang.control.Optional;

public class IteratorTest {

    @Test
    public void ofInteger() {
        final Iterator<Integer> numbers = Iterator.of(1, 2, 3, 4, 5);

        final Optional<Integer> firstMatch = numbers.findFirst(i -> i % 2 == 0);
        final Optional<Integer> lastMatch = numbers.findLast(i -> i % 2 == 0);

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
}
