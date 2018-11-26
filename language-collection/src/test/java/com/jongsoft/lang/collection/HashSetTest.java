package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HashSetTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void add() {
        final Set<String> strings = HashSet.<String>empty()
                .add("one")
                .add("two")
                .add("one");

        assertThat(strings.size(), equalTo(2));
        assertThat(strings, hasItems("one", "two"));
    }

    @Test
    public void indexOf() {
        int index = HashSet.<String>empty()
                .add("one")
                .add("two")
                .add("three")
                .indexOf("two");

        assertThat(index, equalTo(1));
    }

    @Test
    public void firstIndexOf() {
        Set<String> set = HashSet.<String>empty()
                .add("one")
                .add("two")
                .add("three");

        int index = set.firstIndexOf(str -> str.length() == 5);
        int noIndex = set.indexOf("five");

        assertThat(index, equalTo(2));
        assertThat(noIndex, equalTo(-1));
    }

    @Test
    public void get() {
        Set<String> strings = HashSet.of("one", "two", "one", "three");

        assertThat(strings.get(), equalTo("one"));
        assertThat(strings.get(0), equalTo("one"));
        assertThat(strings.get(1), equalTo("two"));
        assertThat(strings.get(2), equalTo("three"));
    }

    @Test
    public void getOutOfBounds() {
        thrown.expect(IndexOutOfBoundsException.class);

        HashSet.of("one").get(1);
        HashSet.of("one").get(-1);
    }

    @Test
    public void remove() {
        Set<String> strings = HashSet.of("one", "two", "one", "three")
                .remove(2);

        assertThat(strings.size(), equalTo(2));
        assertThat(strings, hasItems("one", "two"));
    }

    @Test
    public void filter() {
        Set<String> filter = HashSet.of("one", "two", "one", "three")
                .filter(s -> s.length() > 3);

        assertThat(filter.size(), equalTo(1));
        assertThat(filter, hasItems(equalTo("three")));
    }

    @Test
    public void map() {
        Set<Integer> lengths = HashSet.of("one", "two", "one", "three")
                .map(String::length);

        assertThat(lengths.size(), equalTo(2));
        assertThat(lengths, hasItems(3, 5));
    }
}
