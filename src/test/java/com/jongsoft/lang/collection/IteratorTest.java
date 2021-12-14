package com.jongsoft.lang.collection;

import com.jongsoft.lang.Collections;
import com.jongsoft.lang.control.Optional;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IteratorTest {

    @Test
    void ofInteger() {
        final Iterator<Integer> numbers = Collections.Iterator(1, 2, 3, 4, 5);

        final Optional<Integer> firstMatch = numbers.first(i -> i % 2 == 0);
        final Optional<Integer> lastMatch = numbers.last(i -> i % 2 == 0);

        assertThat(firstMatch.isPresent()).isTrue();
        assertThat(firstMatch.get()).isEqualTo(2);
        assertThat(lastMatch.isPresent()).isTrue();
        assertThat(lastMatch.get()).isEqualTo(4);
    }

    @Test
    void concatInteger() {
        final Iterator<Integer> numbers = Iterator.concat(Collections.Iterator(1), Collections.Iterator(2));

        assertThat(numbers.hasNext()).isTrue();
        assertThat(numbers.next()).isEqualTo(1);
        assertThat(numbers.hasNext()).isTrue();
        assertThat(numbers.next()).isEqualTo(2);
        assertThat(numbers.hasNext()).isFalse();

        // reset test
        numbers.reset();
        assertThat(numbers.hasNext()).isTrue();
        assertThat(numbers.next()).isEqualTo(1);
        assertThat(numbers.hasNext()).isTrue();
        assertThat(numbers.next()).isEqualTo(2);
        assertThat(numbers.hasNext()).isFalse();
    }

    @Test
    void empty() {
        final Iterator<Object> empty = Collections.Iterator();

        assertThat(empty.hasNext()).isFalse();
        assertThatThrownBy(empty::next)
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void filter() {
        Iterator<Integer> iterator = Collections.Iterator(1, 2, 3, 4)
                .filter(x -> x % 2 == 0);

        assertThat(iterator.next()).isEqualTo(2);
        assertThat(iterator.next()).isEqualTo(4);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    void get() {
        final Integer number = Collections.Iterator(1, 2, 3, 4, 5).get();
        assertThat(number).isEqualTo(1);
    }

    @Test
    public void map() {
        final Iterator<Integer> mapped = Collections.Iterator("one", "two", "three", "four")
                .map(String::length);

        assertThat(mapped.next()).isEqualTo(3);
        assertThat(mapped.next()).isEqualTo(3);
        assertThat(mapped.next()).isEqualTo(5);
        assertThat(mapped.next()).isEqualTo(4);
        assertThat(mapped.hasNext()).isFalse();
    }

    @Test
    public void toNativeArray() {
        Object[] strings = Collections.Iterator("one", "two").toNativeArray();

        assertThat(strings.length).isEqualTo(2);
        assertThat(strings[0]).isEqualTo("one");
        assertThat(strings[1]).isEqualTo("two");
    }
    
    @Test
    public void firstNoElements() {
        final Optional<Object> match = Collections.Iterator()
                .first(s -> true);

        assertThat(match.isPresent()).isFalse();
    }
}
