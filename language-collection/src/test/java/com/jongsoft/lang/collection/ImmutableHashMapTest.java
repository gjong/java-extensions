package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

public class ImmutableHashMapTest {

    @Test
    public void empty() {
        final ImmutableMap empty = ImmutableHashMap.create();

        assertThat(empty.size(), equalTo(0));
    }

    @Test
    public void containsKey() {
        ImmutableMap<String, String> subject = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three");

        assertThat(subject.containsKey("one"), equalTo(true));
        assertThat(subject.containsKey("two"), equalTo(true));
        assertThat(subject.containsKey("three"), equalTo(false));
        assertThat(subject.containsKey(null), equalTo(false));
    }

    @Test
    public void contains() {
        ImmutableMap<String, String> subject = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three");

        assertThat(subject.contains("one"), equalTo(false));
        assertThat(subject.contains("two"), equalTo(true));
        assertThat(subject.contains("three"), equalTo(true));
    }

    @Test
    public void size() {
        ImmutableMap<String, String> subject = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three");

        assertThat(subject.size(), equalTo(2));
    }

}
