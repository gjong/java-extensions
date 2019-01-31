package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

import java.util.NoSuchElementException;

import com.jongsoft.lang.API;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.jongsoft.lang.control.Optional;

public class IteratorTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void ofInteger() {
        final Iterator<Integer> numbers = API.Iterator(1, 2, 3, 4, 5);

        final Optional<Integer> firstMatch = numbers.first(i -> i % 2 == 0);
        final Optional<Integer> lastMatch = numbers.last(i -> i % 2 == 0);

        assertThat(firstMatch.isPresent(), equalTo(true));
        assertThat(firstMatch.get(), equalTo(2));
        assertThat(lastMatch.isPresent(), equalTo(true));
        assertThat(lastMatch.get(), equalTo(4));
    }

    @Test
    public void concatInteger() {
        final Iterator<Integer> numbers = Iterator.concat(API.Iterator(1), API.Iterator(2));

        assertThat(numbers.hasNext(), equalTo(true));
        assertThat(numbers.next(), equalTo(1));
        assertThat(numbers.hasNext(), equalTo(true));
        assertThat(numbers.next(), equalTo(2));
        assertThat(numbers.hasNext(), equalTo(false));

        // reset test
        numbers.reset();
        assertThat(numbers.hasNext(), equalTo(true));
        assertThat(numbers.next(), equalTo(1));
        assertThat(numbers.hasNext(), equalTo(true));
        assertThat(numbers.next(), equalTo(2));
        assertThat(numbers.hasNext(), equalTo(false));
    }

    @Test
    public void empty() {
        thrown.expect(NoSuchElementException.class);

        final Iterator<Object> empty = API.Iterator();

        assertThat(empty.hasNext(), equalTo(false));
        empty.next();
    }

    @Test
    public void filter() {
        Iterator<Integer> iterator = API.Iterator(1, 2, 3, 4)
                .filter(x -> x % 2 == 0);

        assertThat(iterator.next(), equalTo(2));
        assertThat(iterator.next(), equalTo(4));
        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void get() {
        final Integer number = API.Iterator(1, 2, 3, 4, 5).get();
        assertThat(number, equalTo(1));
    }

    @Test
    public void map() {
        final Iterator<Integer> mapped = API.Iterator("one", "two", "three", "four")
                .map(String::length);

        assertThat(mapped.next(), equalTo(3));
        assertThat(mapped.next(), equalTo(3));
        assertThat(mapped.next(), equalTo(5));
        assertThat(mapped.next(), equalTo(4));
        assertThat(mapped.hasNext(), equalTo(false));
    }

    @Test
    public void toNativeArray() {
        Object[] strings = API.Iterator("one", "two").toNativeArray();

        assertThat(strings.length, equalTo(2));
        assertThat(strings[0], equalTo("one"));
        assertThat(strings[1], equalTo("two"));
    }
    
    @Test
    public void firstNoElements() {
        final Optional<Object> match = API.Iterator()
                .first(s -> true);

        assertThat(match.isPresent(), equalTo(false));
    }
}
