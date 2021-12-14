package com.jongsoft.lang.collection;

import com.jongsoft.lang.Collections;
import com.jongsoft.lang.collection.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class HashMapTest {

    @Test
    void empty() {
        final Map empty = Collections.Map();

        assertThat(empty).isEmpty();
    }

    @Test
    void containsKey() {
        Map<String, String> subject = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three");

        assertThat(subject.containsKey("one")).isTrue();
        assertThat(subject.containsKey("two")).isTrue();
        assertThat(subject.containsKey("three")).isFalse();
        assertThat(subject.containsKey(null)).isFalse();
    }

    @Test
    void containsValue() {
        Map<String, String> subject = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three");

        assertThat(subject.containsValue("one")).isFalse();
        assertThat(subject.containsValue("two")).isTrue();
        assertThat(subject.containsValue("three")).isTrue();
    }

    @Test
    void size() {
        Map<String, String> subject = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three");

        assertThat(subject).hasSize(2);
    }

    @Test
    void remove() {
        Map<String, String> subject = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three")
                .remove("one");

        assertThat(subject).hasSize(1);
        assertThat(subject.get("two")).isEqualTo("three");
        assertThat(subject.containsKey("one")).isFalse();
        assertThat(subject.remove("none")).isEqualTo(subject);
    }

    @Test
    void putDuplicateKey() {
        Map<String, String> subject = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three")
                .put("one", "five");

        assertThat(subject).hasSize(2);
        assertThat(subject.get("one")).isEqualTo("five");
    }

    @Test
    void get() {
        Map<String, String> subject = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three");

        assertThat(subject.get().getFirst()).isEqualTo("one");
        assertThat(subject.get("one")).isEqualTo("two");
    }

    @Test
    void stream() {
        List<Pair<String, String>> collected = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three")
                .stream()
                .collect(Collectors.toList());

        assertThat(collected).hasSize(2);
        assertThat(collected.get(0).getFirst()).isEqualTo("one");
        assertThat(collected.get(0).getSecond()).isEqualTo("two");
        assertThat(collected.get(1).getFirst()).isEqualTo("two");
        assertThat(collected.get(1).getSecond()).isEqualTo("three");
    }

    @Test
    void streamValue() {
        List<String> collected = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three")
                .valueStream()
                .collect(Collectors.toList());

        assertThat(collected)
                .hasSize(2)
                .containsExactly("two", "three");
    }

    @Test
    void map() {
        Collection<String> collected = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three")
                .map(Pair::getSecond);

        assertThat(collected)
                .hasSize(2)
                .containsExactly("two", "three");
    }

    @Test
    void filter() {
        final Map<String, String> result = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three")
                .filter(p -> p.getSecond().length() == 3);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get("one")).isEqualTo("two");
    }

    @Test
    void reject() {
        Map<String, String> result = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three")
                .reject(e -> e.getSecond().length() == 3);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get("two")).isEqualTo("three");
    }

    @Test
    void split() {
        Pair<? extends Map<Integer, String>, ? extends Map<Integer, String>> split =
                Collections.Map(1, "two")
                        .put(2, "three")
                        .put(3, "four")
                        .split(x -> x.getFirst() % 2 == 0);

        assertThat(split.getFirst()).hasSize(1);
        assertThat(split.getFirst().containsKey(2)).isTrue();
        assertThat(split.getSecond()).hasSize(2);
        assertThat(split.getSecond().containsKey(1)).isTrue();
        assertThat(split.getSecond().containsKey(3)).isTrue();
    }

    @Test
    void iterator() {
        Iterator<Pair<String, String>> stringIt = Collections.Map("one", "two")
                .put("two", "three")
                .iterator();

        assertThat(stringIt.next().getSecond()).isEqualTo("two");
        assertThat(stringIt.next().getSecond()).isEqualTo("three");
        assertThat(stringIt.hasNext()).isFalse();
    }

    @Test
    void toJava() {
        java.util.Map result = Collections.<String, String>Map()
                .put("one", "two")
                .put("two", "three")
                .toJava();

        assertThat(result)
                .hasSize(2)
                .extractingByKey("one").isEqualTo("two");
    }

    @Test
    void testToString() {
        String result = Collections.Map()
                .put("one", "two")
                .put("two", "three")
                .toString();

        assertThat(result).isEqualTo("Map {"
                        + System.lineSeparator()
                        + "one : two"
                        + System.lineSeparator()
                        + "two : three"
                        + System.lineSeparator()
                        + "}");
    }
}
