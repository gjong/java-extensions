package com.jongsoft.lang.collection;

import com.jongsoft.lang.Collections;
import com.jongsoft.lang.collection.tuple.Pair;
import com.jongsoft.lang.control.Optional;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArrayTest {

    @Test
    void empty() {
        Sequence empty = Collections.List();

        assertThatThrownBy(() -> empty.iterator().next())
                .isInstanceOf(NoSuchElementException.class);

        assertThat(empty).isEmpty();
        assertThat(empty.iterator().hasNext()).isFalse();
    }

    @Test
    void pipeline() {
        Pipeline<Integer> pipe = Collections.List(1, 2, 3, 4)
                .pipeline()
                .map(x -> x * 2);

        Pipeline<Integer> pipe1 = pipe.filter(x -> x % 2 == 0);
        Pipeline<String> pipe2 = pipe.map(String::valueOf);

        Iterator<Integer> iterator = pipe1.iterator();
        assertThat(iterator.next()).isEqualTo(2);
        assertThat(iterator.next()).isEqualTo(4);
        assertThat(iterator.next()).isEqualTo(6);
        assertThat(iterator.next()).isEqualTo(8);
        assertThat(iterator.hasNext()).isFalse();

        Iterator<String> iterator1 = pipe2.iterator();
        assertThat(iterator1.next()).isEqualTo("2");
        assertThat(iterator1.next()).isEqualTo("4");
        assertThat(iterator1.next()).isEqualTo("6");
        assertThat(iterator1.next()).isEqualTo("8");
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    void headNoElement() {
        assertThatThrownBy(() -> Collections.List().head())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void head() {
        Integer head = Collections.List(1, 2).head();
        assertThat(head).isEqualTo(1);
    }

    @Test
    void tailNoElements() {
        assertThatThrownBy(() -> Collections.List().tail())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void tail() {
        assertThat(Collections.List(1).tail()).isEmpty();
        assertThat(Collections.List(1, 2).tail().head()).isEqualTo(2);
    }

    @Test
    void singleInt() {
        Sequence<Integer> ints = Collections.List(5);

        assertThat(ints).hasSize(1);
        assertThat(ints.isSingleValued()).isFalse();
        assertThat(ints.iterator().next()).isEqualTo(5);
    }

    @Test
    void singleIntOutOfBounds() {
        assertThatThrownBy(() -> Collections.List(5).get(1))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessage("1 is not in the bounds of 0 and 1");
    }

    @Test
    void insertAt0Int() {
        Sequence<Integer> insert = Collections.List(5).insert(0, 2);

        assertThat(insert)
                .hasSize(2)
                .containsExactly(2, 5);
    }

    @Test
    void insertAt1Int() {
        Sequence<Integer> insert = Collections.List(5).insert(1, 2);

        assertThat(insert)
                .hasSize(2)
                .containsExactly(5, 2);
    }

    @Test
    void insertAt2Int() {
        Sequence<Integer> insert = Collections.List(1, 2, 3, 4, 5).insert(2, 12);

        assertThat(insert)
                .hasSize(6)
                .containsExactly(1, 2, 12, 3, 4, 5);
    }

    @Test
    void append() {
        Sequence<Integer> source = Collections.List(5);
        Sequence<Integer> add = source.append(6);

        assertThat(source)
                .hasSize(1)
                .isNotEmpty()
                .containsExactly(5);
        assertThat(add)
                .hasSize(2)
                .containsExactly(5, 6);
    }

    @Test
    void prepend() {
        Sequence<Integer> result = Collections.List(2, 3, 4).prepend(1);

        assertThat(result)
                .hasSize(4)
                .containsExactly(1, 2, 3, 4);
    }

    @Test
    void addAll() {
        Sequence<Integer> values = Collections.List(1, 2, 3)
                .union(Arrays.asList(4, 5, 6));

        assertThat(values).hasSize(6)
                .containsExactly(1, 2, 3, 4, 5, 6);
    }

    @Test
    void removeIntAt2() {
        Sequence<Integer> removed = Collections.List(1, 2, 3, 4, 5).remove(2);

        assertThat(removed)
                .hasSize(4)
                .containsExactly(1, 2, 4, 5);
    }

    @Test
    void removeByElement() {
        Sequence<String> original = Collections.List("test", "string", "one");
        Sequence<String> afterRemove = original.remove("string");

        assertThat(original)
                .hasSize(3)
                .isNotEqualTo(afterRemove);
        assertThat(afterRemove)
                .hasSize(2)
                .doesNotContain("string");
        assertThat(original.remove("four")).isEqualTo(original);
    }

    @Test
    void removeIntAt6OutOfBounds() {
        assertThatThrownBy(() -> Collections.List(1, 2, 3, 4, 5).remove(6))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessage("6 is not in the bounds of 0 and 5");
    }

    @Test
    void filterInt5() {
        Sequence<Integer> noFives = Collections.List(1, 5, 34, 4, 5, 23, 4, 5)
                .filter(i -> 5 != i);

        assertThat(noFives)
                .hasSize(5)
                .containsExactly(1, 34, 4, 23, 4);
    }

    @Test
    void distinct() {
        Set<Integer> numbers = Collections.List(1, 2, 3, 1, 4, 2, 5)
                .distinct();

        assertThat(numbers)
                .hasSize(5)
                .containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void distinctBy() {
        Set<String> strings = Collections.List("one", "two", "three")
                .distinctBy(Comparator.comparing(String::length));

        assertThat(strings)
                .hasSize(2)
                .containsExactly("one", "three");
    }

    @Test
    void sorted() {
        var sorted = Collections.List(3, 2, 4, 1)
                .sorted();

        assertThat(sorted).containsExactly(1, 2, 3, 4);
    }

    @Test
    void median() {
        var median = Collections.List(1, 2, 3, 4, 5)
                .median();

        assertThat(median).isEqualTo(3.0);
    }

    @Test
    void min() {
        var min = Collections.List(1, 2, 3, 4, 5)
                .min();

        assertThat(min.get()).isEqualTo(1);
        assertThat(Collections.List("apple", "pear").min().get())
                .isEqualTo("apple");
    }

    @Test
    void reject() {
        Sequence<Integer> noFives = Collections.List(1, 2, 3, 4, 5)
                .reject(i -> i % 2 == 0);

        assertThat(noFives)
                .hasSize(3)
                .containsExactly(1, 3, 5);
    }

    @Test
    void ofList() {
        Sequence<Integer> integers = Collections.List(Arrays.asList(0, 1, 2, 3, 4, 5));
        assertThat(integers).hasSize(6);
    }

    @Test
    void ofIterable() {
        Sequence<Integer> integers = Collections.List(() -> java.util.List.of(5).iterator());

        assertThat(integers)
                .hasSize(1)
                .containsExactly(5);
    }

    @Test
    void containsNull() {
        assertThat(Collections.List(1, 2, 3, 4, 5, null).contains(null)).isTrue();
        assertThat(Collections.List(1, 2, 3, 4, 5).contains(null)).isFalse();
    }

    @Test
    void contains5() {
        assertThat(Collections.List(1, 2, 3, 4, 5, null).contains(5)).isEqualTo(true);
        assertThat(Collections.List(1, 2, 3, 4).contains(5)).isFalse();
    }

    @Test
    void containsAll() {
        assertThat(Collections.List(1, 2, 3, 4).containsAll(Collections.List(2, 3))).isTrue();
    }

    @Test
    void count() {
        int count = Collections.List(1, 2, 3, 4)
                .count(x -> x % 2 == 0);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void summing() {
        long total = Collections.List(1, 2, 3, 4)
                .summing(Long::sum);
        assertThat(total).isEqualTo(10L);
    }

    @Test
    void fold() {
        assertThat(Collections.List("t", "e", "s", "t").fold("!", (x, y) -> x + y))
                .isEqualTo("!test");
        assertThat(Collections.List("t", "e", "s", "t").foldLeft("!", (x, y) -> x + y))
                .isEqualTo("!test");
    }

    @Test
    void foldRight() {
        assertThat(Collections.List("t", "e", "s", "t").foldRight("!", (x, y) -> x + y))
                .isEqualTo("tset!");
    }

    @Test
    void reduceLeft() {
        String reduce = Collections.List("t", "e", "s", "t").reduce((x, y) -> x + y);
        String reduceLeft = Collections.List("t", "e", "s", "t").reduceLeft((x, y) -> x + y);

        assertThat(reduce).isEqualTo("test");
        assertThat(reduceLeft).isEqualTo("test");
    }

    @Test
    void indexOf5() {
        assertThat(Collections.List(1, 2, 3, 4, 5, null).indexOf(5)).isEqualTo(4);
        assertThat(Collections.List(1, 2, 3, 4).indexOf(5)).isEqualTo(-1);
    }

    @Test
    void map() {
        Sequence<Integer> mapped = Collections.List("test", "two")
                .map(String::length);

        assertThat(mapped).hasSize(2)
                .containsExactly(4, 3);
    }

    @Test
    void orElse() {
        Sequence<String> result = Collections.List("test")
                .orElse(Collections.List("two"));

        Sequence<String> supplied = Collections.List("test")
                .orElse(Collections.List("two"));

        assertThat(result).contains("test");
        assertThat(supplied).contains("test");
    }

    @Test
    void sum() {
        final Optional<Double> sum = Collections.List(1, 3, 3).sum();
        assertThat(sum.get()).isEqualTo(7.0);
        assertThat(Collections.List(1.0, 10e100, 2.0, -10e100).sum().get()).isEqualTo(3.0);

        assertThat(Double.isNaN(Collections.List(1.0, Double.NaN).sum().get())).isTrue();
    }

    @Test
    void sum_empty() {
        final Optional<Double> sum = Collections.List().sum();
        assertThat(sum.isPresent()).isFalse();
    }

    @Test
    void average() {
        assertThat(Collections.List().average().isPresent()).isFalse();
        assertThat(Collections.List(1, 2, 3).average().get()).isEqualTo(2.0);
        assertThat(Collections.List(1.0, 10e100, 2.0, -10e100).average().get()).isEqualTo(0.75);

        assertThat(Double.isNaN(Collections.List(1.0, Double.NaN).average().get())).isTrue();
    }

    @Test
    void average_NoNumber() {
        assertThatThrownBy(() -> Collections.List("st", "tes").average())
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void sum_NoNumber() {
        assertThatThrownBy(() -> Collections.List("st", "tes").sum())
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void allNone() {
        Sequence<String> result = Collections.List("o", "n", "b");

        assertThat(result.none(x -> x.length() == 2)).isTrue();
        assertThat(result.all(x -> x.length() == 1)).isTrue();
    }

    @Test
    void groupBy() {
        Map<Integer, ? extends Sequence<String>> result = Collections.List("one", "two", "three", "four")
                .groupBy(String::length);

        assertThat(result).hasSize(3);
        assertThat(result.get(3))
                .hasSize(2)
                .containsExactly("one", "two");
        assertThat(result.get(4))
                .hasSize(1)
                .containsExactly("four");
        assertThat(result.get(5))
                .hasSize(1)
                .containsExactly("three");
    }

    @Test
    void findLastFirst() {
        final Sequence<Integer> array = Collections.List(1, 2, 3, 4, 5);

        Optional<Integer> lastFound = array.last(i -> i % 2 == 0);
        assertThat(lastFound.isPresent()).isTrue();
        assertThat(lastFound.get()).isEqualTo(4);

        Optional<Integer> lastNotFound = array.last(i -> i % 9 == 0);
        assertThat(lastNotFound.isPresent()).isFalse();

        Optional<Integer> firstFound = array.first(i -> i % 2 == 0);
        assertThat(firstFound.isPresent()).isTrue();
        assertThat(firstFound.get()).isEqualTo(2);
    }

    @Test
    void reverse() {
        final Sequence<Integer> result = Collections.List(1, 2, 3).reverse();

        assertThat(result)
                .hasSize(3)
                .containsExactly(3, 2, 1);
    }

    @Test
    void replace() {
        final Sequence<Integer> result = Collections.List(1, 2, 3)
                .replace(1, 5);

        assertThat(result)
                .hasSize(3)
                .containsExactly(1, 5, 3);
    }

    @Test
    void replaceIf() {
        final Sequence<Integer> result = Collections.List(1, 2, 3)
                .replaceIf(el -> el == 2, 5);

        assertThat(result)
                .hasSize(3)
                .containsExactly(1, 5, 3);
    }

    @Test
    void split() {
        Pair<? extends Sequence<Integer>, ? extends Sequence<Integer>> split =
                Collections.List(1, 2, 3)
                        .split(v -> v % 2 == 0);

        assertThat(split.getFirst())
                .hasSize(1)
                .containsExactly(2);
        assertThat(split.getSecond())
                .hasSize(2)
                .containsExactly(1, 3);
    }

    @Test
    void toJava() {
        java.util.List<Integer> integers = Collections.List(1, 2, 3, 4)
                .toJava();

        assertThat(integers)
                .isInstanceOf(ArrayList.class)
                .hasSize(4)
                .containsExactly(1, 2, 3, 4);
    }

    @Test
    void toStringTest() {
        Sequence<String> myStrings = Collections.List("test", "string", "one")
                .filter(s -> s.length() == 3)
                .remove("test");

        assertThat(myStrings).hasToString("Sequence[one]");
    }

    @Test
    void equalTest() {
        assertThat(Collections.List(1, 2, 3)).isEqualTo(Collections.List(1, 2, 3));
        assertThat(Collections.List("test", 2, 3)).isEqualTo(Collections.List(2, "test", 3));
        assertThat(Collections.List("test", 2, 3)).isNotEqualTo(Collections.List("test", 3));
        assertThat(Collections.List("test", 2, 3)).isNotEqualTo(Collections.List("test", 3, null));
    }

    @Test
    void hashCodeTest() {
        assertThat(Collections.List()).hasSameHashCodeAs(19);
        assertThat(Collections.List(null, 1)).hasSameHashCodeAs(20);
    }
}
