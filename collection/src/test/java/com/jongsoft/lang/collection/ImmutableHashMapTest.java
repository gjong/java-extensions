package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import com.jongsoft.lang.collection.tuple.Tuple;

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
    public void containsValue() {
        ImmutableMap<String, String> subject = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three");

        assertThat(subject.containsValue("one"), equalTo(false));
        assertThat(subject.containsValue("two"), equalTo(true));
        assertThat(subject.containsValue("three"), equalTo(true));
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
        assertThat(subject.remove("none"), equalTo(subject));
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
    public void get() {
        ImmutableMap<String, String> subject = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three");

        assertThat(subject.get().getFirst(), equalTo("one"));
        assertThat(subject.get("one"), equalTo("two"));
    }

    @Test
    public void stream() {
        List<Tuple.Pair<String, String>> collected = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three")
                .stream()
                .collect(Collectors.toList());

        assertThat(collected.size(), equalTo(2));
        assertThat(collected.get(0).getFirst(), equalTo("one"));
        assertThat(collected.get(0).getSecond(), equalTo("two"));
        assertThat(collected.get(1).getFirst(), equalTo("two"));
        assertThat(collected.get(1).getSecond(), equalTo("three"));
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

    @Test
    public void map() {
        Collection<String> collected = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three")
                .map(Tuple.Pair::getSecond);

        assertThat(collected.size(), equalTo(2));
        assertThat(collected, hasItems("two", "three"));
    }

    @Test
    public void filter() {
        final ImmutableMap<String, String> result = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three")
                .filter(p -> p.getSecond().length() == 3);

        assertThat(result.size(), equalTo(1));
        assertThat(result.get("one"), equalTo("two"));
    }

    @Test
    public void iterator() {
        Iterator<Tuple.Pair<String, String>> stringIt = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three")
                .iterator();

        assertThat(stringIt.next().getSecond(), equalTo("two"));
        assertThat(stringIt.next().getSecond(), equalTo("three"));
        assertThat(stringIt.hasNext(), equalTo(false));
    }

    @Test
    public void toJava() {
        Map<String, String> result = ImmutableHashMap.<String, String>create()
                .put("one", "two")
                .put("two", "three")
                .toJava();

        assertThat(result.size(), equalTo(2));
        assertThat(result.get("one"), equalTo("two"));
        assertThat(result.get("two"), equalTo("three"));
    }
}
