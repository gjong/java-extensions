package com.jongsoft.lang.collection;

import com.jongsoft.lang.Collections;
import com.jongsoft.lang.collection.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HashSetTest {

    @Test
    void append() {
        final Set<String> strings = Collections.<String>Set()
                .append("one")
                .append("two")
                .append("one");

        assertThat(strings)
                .hasSize(2)
                .containsExactly("one", "two");
    }

    @Test
    void distinctBy() {
        Set<String> strings = Collections.Set("one", "two", "three")
                .distinctBy(Comparator.comparing(String::length));

        assertThat(strings)
                .hasSize(2)
                .containsExactly("one", "three");
    }

    @Test
    void union() {
        Set<Integer> result = Collections.Set(1, 2, 3)
                .union(Collections.Set(3, 4, 5));

        assertThat(result).hasSize(5)
                .containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void intersect() {
        Set<Integer> result = Collections.Set(1, 2, 3)
                .intersect(Collections.Set(3, 4, 5));

        assertThat(result)
                .hasSize(1)
                .containsExactly(3);
    }

    @Test
    void intersectEmpty() {
        Set<Integer> result = Collections.Set(1, 2, 3)
                .intersect();

        assertThat(result).isEmpty();
    }

    @Test
    void intersectMultiple() {
        Set<Integer> result = Collections.Set(1, 2, 3)
                .intersect(Collections.List(3, 4, 5), Collections.List(4, 7, 3));

        assertThat(result).hasSize(1)
                .containsExactly(3);
    }

    @Test
    void complement() {
        Set<Integer> result = Collections.Set(1, 2, 3, 4)
                .complement(Collections.Set(3, 2));

        assertThat(result)
                .hasSize(2)
                .containsExactly(1, 4);
    }

    @Test
    void sorted() {
        var sorted = Collections.Set(3, 2, 4, 1)
                .sorted();

        assertThat(sorted.get(0)).isEqualTo(1);
        assertThat(sorted.get(1)).isEqualTo(2);
        assertThat(sorted.get(2)).isEqualTo(3);
        assertThat(sorted.get(3)).isEqualTo(4);
    }

    @Test
    void replace() {
        var result = Collections.Set(1, 2, 3)
                .replace(1, 5);

        assertThat(result)
                .hasSize(3)
                .containsExactly(1, 5, 3);
    }

    @Test
    void replaceIf() {
        var result = Collections.Set(1, 2, 3)
                .replaceIf(el -> el == 2, 5);

        assertThat(result)
                .hasSize(3)
                .containsExactly(1, 5, 3);
    }

    @Test
    void complementMultiple() {
        Set<Integer> result = Collections.Set(1, 2, 3, 4)
                .complement(Collections.Set(2), Collections.Set(3));

        assertThat(result)
                .hasSize(2)
                .containsExactly(1, 4);

        assertThat(Collections.Set(1, 2, 3).complement())
                .containsExactly(1, 2, 3);
    }

    @Test
    void toJava() {
        final java.util.Set<Integer> ints = Collections.Set(1, 2, 3, 4)
                .toJava();

        assertThat(ints)
                .hasSize(4)
                .containsExactly(1, 2, 3, 4);
    }

    @Test
    void indexOf() {
        int index = Collections.<String>Set()
                .append("one")
                .append("two")
                .append("three")
                .indexOf("two");

        assertThat(index).isEqualTo(1);
    }

    @Test
    void firstIndexOf() {
        Set<String> set = Collections.<String>Set()
                .append("one")
                .append("two")
                .append("three");

        int index = set.firstIndexWhere(str -> str.length() == 5);
        int noIndex = set.indexOf("five");

        assertThat(index).isEqualTo(2);
        assertThat(noIndex).isEqualTo(-1);
    }

    @Test
    void get() {
        Set<String> strings = Collections.Set("one", "two", "one", "three");

        assertThat(strings.get()).isEqualTo("one");
        assertThat(strings.get(0)).isEqualTo("one");
        assertThat(strings.get(1)).isEqualTo("two");
        assertThat(strings.get(2)).isEqualTo("three");
    }

    @Test
    void getOutOfBounds() {
        assertThatThrownBy(() -> Collections.Set("one").get(1))
                .isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> Collections.Set("one").get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void groupBy() {
        final Map<Integer, ? extends List<Integer>> pairs = Collections.Set(1, 2, 3, 4, 5).groupBy(x -> x % 2);

        assertThat(pairs).hasSize(2);
        assertThat(pairs.containsKey(0)).isTrue();
        assertThat(pairs.get(0).size()).isEqualTo(2);
        assertThat(pairs.get(0)).containsExactly(2, 4);
        assertThat(pairs.containsKey(1)).isTrue();
        assertThat(pairs.get(1).size()).isEqualTo(3);
        assertThat(pairs.get(1)).containsExactly(1, 3, 5);
    }

    @Test
    void headNoElement() {
        assertThatThrownBy(() -> Collections.Set().head())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void head() {
        Integer head = Collections.Set(1, 2).head();
        assertThat(head).isEqualTo(1);
    }

    @Test
    void tailNoElements() {
        assertThatThrownBy(() -> Collections.Set().tail())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void tail() {
        assertThat(Collections.Set(1).tail()).isEmpty();
        assertThat(Collections.Set(1, 1, 2).tail().head()).isEqualTo(2);
    }

    @Test
    void remove() {
        Set<String> strings = Collections.Set("one", "two", "one", "three")
                .remove(2);

        assertThat(strings)
                .hasSize(2)
                .containsExactly("one", "two");
    }

    @Test
    void filter() {
        Set<String> filter = Collections.Set("one", "two", "one", "three")
                .filter(s -> s.length() > 3);

        assertThat(filter)
                .hasSize(1)
                .containsExactly("three");
    }

    @Test
    void reject() {
        Set<Integer> noFives = Collections.Set(1, 2, 3, 4, 5)
                .reject(i -> i % 2 == 0);

        assertThat(noFives)
                .hasSize(3)
                .containsExactly(1, 3, 5);
    }

    @Test
    void split() {
        Pair<? extends Collection<Integer>, ? extends Collection<Integer>> split =
                Collections.Set(1, 2, 3, 4, 5, 5)
                        .split(i -> i % 2 == 0);

        assertThat(split.getFirst())
                .hasSize(2)
                .containsExactly(2, 4);
        assertThat(split.getSecond())
                .hasSize(3)
                .containsExactly(1, 3, 5);
    }

    @Test
    void map() {
        Set<Integer> lengths = Collections.Set("one", "two", "one", "three")
                .map(String::length);

        assertThat(lengths)
                .hasSize(2)
                .containsExactly(3, 5);
    }

    @Test
    void orElse() {
        Set<String> result = Collections.Set("test")
                .orElse(Collections.List("two"));

        Set<String> supplied = Collections.Set("test")
                .orElse(Collections.List("two"));

        assertThat(result).contains("test");
        assertThat(supplied).contains("test");
    }


    @Test
    void fold() {
        String folded = Collections.Set("a", "b", "a", "c").fold("!", (xs, y) -> xs + y);
        String leftFolded = Collections.Set("a", "b", "a", "c").foldLeft("!", (xs, y) -> xs + y);

        assertThat(folded).isEqualTo("!abc");
        assertThat(leftFolded).isEqualTo("!abc");
    }

    @Test
    void rightFold() {
        String folded = Collections.Set("a", "b", "a", "c").foldRight("!", (xs, y) -> xs + y);

        assertThat(folded).isEqualTo("cba!");
    }

    @Test
    void toStringTest() {
        Set<String> myStrings = Collections.Set("test-a", "test-b", "one")
                .filter(s -> s.length() == 6);

        assertThat(myStrings).hasToString("Set[test-a, test-b]");
    }

    @Test
    void equalTest() {
        assertThat(Collections.Set(1, 2, 3)).isEqualTo(Collections.Set(1, 2, 3));
        assertThat(Collections.Set("test", 2, 3)).isEqualTo(Collections.Set(2, "test", 3));
        assertThat(Collections.Set("test", 2, 3)).isNotEqualTo(Collections.Set("test", 3));
    }

    @Test
    void hashCodeTest() {
        assertThat(Collections.Set().hashCode()).isEqualTo(21);
    }
}
