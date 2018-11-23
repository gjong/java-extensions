package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class HashSetTest {

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
    public void firstIndexOf() {
         int index = HashSet.<String>empty()
                .add("one")
                .add("two")
                .add("three")
                .firstIndexOf(str -> str.length() == 5);

        assertThat(index, equalTo(2));
    }

    @Test
    public void get() {
        Set<String> strings = HashSet.of("one", "two", "one", "three");

        assertThat(strings.get(0), equalTo("one"));
        assertThat(strings.get(1), equalTo("two"));
        assertThat(strings.get(2), equalTo("three"));
    }
}
