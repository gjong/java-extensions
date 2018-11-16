package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    public void remove() {
        ImmutableMap<String, String> subject = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three")
                .remove("one");

        assertThat(subject.size(), equalTo(1));
        assertThat(subject.get("two"), equalTo("three"));
        assertThat(subject.containsKey("one"), equalTo(false));
    }

    @Test
    public void putDuplicateKey() {
        ImmutableMap<String, String> subject = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three")
                .put("one", "five");

        assertThat(subject.size(), equalTo(2));
        assertThat(subject.get("one"), equalTo("five"));
    }

    @Test
    public void stream() {
        List<ImmutableMap.Entry<String, String>> collected = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three")
                .stream()
                .collect(Collectors.toList());

        assertThat(collected.size(), equalTo(2));
        assertThat(collected.get(0).key(), equalTo("one"));
        assertThat(collected.get(0).value(), equalTo("two"));
        assertThat(collected.get(1).key(), equalTo("two"));
        assertThat(collected.get(1).value(), equalTo("three"));
    }

    @Test
    public void streamValue() {
        List<String> collected = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three")
                .valueStream()
                .collect(Collectors.toList());

        assertThat(collected.size(), equalTo(2));
        assertThat(collected, hasItems("two", "three"));
    }
}
