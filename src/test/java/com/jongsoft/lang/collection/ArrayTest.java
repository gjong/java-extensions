package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

import java.util.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jongsoft.lang.control.Optional;

public class ArrayTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void empty() {
        thrown.expect(NoSuchElementException.class);
        Sequence empty = Array.empty();

        empty.iterator().next();

        assertThat(empty.size(), equalTo(0));
        assertThat(empty.iterator().hasNext(), equalTo(false));
    }

    @Test
    public void headNoElement() {
        thrown.expect(NoSuchElementException.class);
        Array.empty().head();
    }

    @Test
    public void head() {
        Integer head = Array.of(1, 2).head();
        assertThat(head, equalTo(1));
    }

    @Test
    public void tailNoElements() {
        thrown.expect(NoSuchElementException.class);
        Array.empty().tail();
    }

    @Test
    public void tail() {
        assertThat(Array.of(1).tail().isEmpty(), equalTo(true));
        assertThat(Array.of(1, 2).tail().head(), equalTo(2));
    }

    @Test
    public void singleInt() {
        Sequence<Integer> ints = Array.of(5);

        assertThat(ints.size(), equalTo(1));
        assertThat(ints.isSingleValued(), equalTo(false));
        assertThat(ints.iterator().next(), equalTo(5));
    }

    @Test
    public void singleIntOutOfBounds() {
        thrown.expect(IndexOutOfBoundsException.class);
        thrown.expectMessage(equalTo("1 is not in the bounds of 0 and 1"));
        Array.of(5).get(1);
    }
    
    @Test
    public void insertAt0Int() {
        Sequence<Integer> insert = Array.of(5).insert(0, 2);

        assertThat(insert.size(), equalTo(2));
        assertThat(insert.get(0), equalTo(2));
        assertThat(insert.get(1), equalTo(5));
    }

    @Test
    public void insertAt1Int() {
        Sequence<Integer> insert = Array.of(5).insert(1, 2);

        assertThat(insert.size(), equalTo(2));
        assertThat(insert.get(0), equalTo(5));
        assertThat(insert.get(1), equalTo(2));
    }

    @Test
    public void insertAt2Int() {
        Sequence<Integer> insert = Array.of(1, 2, 3, 4, 5).insert(2, 12);

        assertThat(insert.size(), equalTo(6));
        assertThat(insert.get(0), equalTo(1));
        assertThat(insert.get(1), equalTo(2));
        assertThat(insert.get(2), equalTo(12));
        assertThat(insert.get(3), equalTo(3));
    }

    @Test
    public void append() {
        Sequence<Integer> source = Array.of(5);
        Sequence<Integer> add = source.append(6);

        assertThat(source.size(), equalTo(1));
        assertThat(source.isEmpty(), equalTo(false));
        assertThat(add.size(), equalTo(2));
        assertThat(add.get(0), equalTo(5));
        assertThat(add.get(1), equalTo(6));
    }

    @Test
    public void prepend() {
        Sequence<Integer> result = Array.of(2, 3, 4).prepend(1);

        assertThat(result.size(), equalTo(4));
        assertThat(result.get(0), equalTo(1));
        assertThat(result.get(1), equalTo(2));
        assertThat(result.get(2), equalTo(3));
        assertThat(result.get(3), equalTo(4));
    }
    
    @Test
    public void addAll() {
        Sequence<Integer> values = Array.of(1, 2, 3)
                .union(Arrays.asList(4, 5, 6));

        assertThat(values.size(), equalTo(6));
        assertThat(values, hasItems(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void removeIntAt2() {
        Sequence<Integer> removed = Array.of(1, 2, 3, 4, 5).remove(2);

        assertThat(removed.size(), equalTo(4));
        assertThat(removed.contains(3), equalTo(false));
    }

    @Test
    public void removeByElement() {
        Sequence<String> original = Array.of("test", "string", "one");
        Sequence<String> afterRemove = original.remove("string");

        assertThat(original, not(equalTo(afterRemove)));
        assertThat(original.size(), equalTo(3));
        assertThat(afterRemove.size(), equalTo(2));
        assertThat(afterRemove.contains("string"), equalTo(false));
        assertThat(original.remove("four"), equalTo(original));
    }

    @Test
    public void removeIntAt6OutOfBounds() {
        thrown.expect(IndexOutOfBoundsException.class);
        thrown.expectMessage(equalTo("6 is not in the bounds of 0 and 5"));

        Array.of(1, 2, 3, 4, 5).remove(6);
    }

    @Test
    public void filterInt5() {
        Sequence<Integer> noFives = Array.of(1, 5, 34, 4, 5, 23, 4, 5)
                .filter(i -> 5 != i);

        assertThat(noFives.size(), equalTo(5));
        assertThat(noFives.get(0), equalTo(1));
        assertThat(noFives.get(1), equalTo(34));
        assertThat(noFives.get(2), equalTo(4));
        assertThat(noFives.get(3), equalTo(23));
    }
    
    @Test
    public void reject() {
        Sequence<Integer> noFives = Array.of(1, 2, 3, 4, 5)
                .reject(i -> i % 2 == 0);

        assertThat(noFives.size(), equalTo(3));
        assertThat(noFives, hasItems(1, 3, 5));
    }

    @Test
    public void ofList() {
        Sequence<Integer> integers = Array.ofAll(Arrays.asList(0, 1, 2, 3, 4, 5));
        assertThat(integers.size(), equalTo(6));
    }

    @Test
    public void ofIterable() {
        Sequence<Integer> integers = Array.ofAll(() -> Collections.singleton(5).iterator());

        assertThat(integers.size(), equalTo(1));
        assertThat(integers.get(0), equalTo(5));
    }

    @Test
    public void containsNull() {
        assertThat(Array.of(1,2,3,4,5,null).contains(null), equalTo(true));
        assertThat(Array.of(1,2,3,4,5).contains(null), equalTo(false));
    }

    @Test
    public void contains5() {
        assertThat(Array.of(1,2,3,4,5,null).contains(5), equalTo(true));
        assertThat(Array.of(1,2,3,4).contains(5), equalTo(false));
    }

    @Test
    public void containsAll() {
        assertThat(Array.of(1, 2, 3, 4).containsAll(Array.of(2, 3)), equalTo(true));
    }

    @Test
    public void count() {
        int count = Array.of(1, 2, 3, 4)
             .count(x -> x % 2 == 0);
        assertThat(count, equalTo(2));
    }

    @Test
    public void fold() {
        assertThat(Array.of("t", "e", "s", "t").fold("!", (x, y) -> x + y), equalTo("!test"));
        assertThat(Array.of("t", "e", "s", "t").foldLeft("!", (x, y) -> x + y), equalTo("!test"));
    }

    @Test
    public void foldRight() {
        assertThat(Array.of("t", "e", "s", "t").foldRight("!", (x, y) -> x + y), equalTo("tset!"));
    }

    @Test
    public void reduceLeft() {
        String reduce = Array.of("t", "e", "s", "t").reduce((x ,y) -> x + y);
        String reduceLeft = Array.of("t", "e", "s", "t").reduceLeft((x ,y) -> x + y);

        assertThat(reduce, equalTo("test"));
        assertThat(reduceLeft, equalTo("test"));
    }

    @Test
    public void indexOf5() {
        assertThat(Array.of(1,2,3,4,5,null).indexOf(5), equalTo(4));
        assertThat(Array.of(1,2,3,4).indexOf(5), equalTo(-1));
    }
    
    @Test
    public void map() {
        Sequence<Integer> mapped = Array.of("test", "two")
                .map(String::length);

        assertThat(mapped.size(), equalTo(2));
        assertThat(mapped, hasItems(4, 3));
    }

    @Test
    public void groupBy() {
        Map<Integer, Sequence<String>> result = Array.of("one", "two", "three", "four")
             .groupBy(String::length);

        assertThat(result.size(), equalTo(3));
        assertThat(result.get(3).size(), equalTo(2));
        assertThat(result.get(3), hasItems("one", "two"));
        assertThat(result.get(4).size(), equalTo(1));
        assertThat(result.get(4), hasItems("four"));
        assertThat(result.get(5).size(), equalTo(1));
        assertThat(result.get(5), hasItems("three"));
    }

    @Test
    public void findLastFirst() {
        final Sequence<Integer> array = Array.of(1, 2, 3, 4, 5);

        Optional<Integer> lastFound = array.last(i -> i % 2 == 0);
        assertThat(lastFound.isPresent(), equalTo(true));
        assertThat(lastFound.get(), equalTo(4));

        Optional<Integer> lastNotFound = array.last(i -> i % 9 == 0);
        assertThat(lastNotFound.isPresent(), equalTo(false));

        Optional<Integer> firstFound = array.first(i -> i % 2 == 0);
        assertThat(firstFound.isPresent(), equalTo(true));
        assertThat(firstFound.get(), equalTo(2));
    }

    @Test
    public void reverse() {
        final Sequence<Integer> result = Array.of(1, 2, 3).reverse();

        assertThat(result.size(), equalTo(3));
        assertThat(result.get(0), equalTo(3));
        assertThat(result.get(1), equalTo(2));
        assertThat(result.get(2), equalTo(1));
    }

    @Test
    public void toJava() {
        List<Integer> integers = Array.of(1, 2, 3, 4)
                .toJava();

        assertThat(integers, instanceOf(ArrayList.class));
        assertThat(integers.size(), equalTo(4));
        assertThat(integers, hasItems(1, 2, 3, 4));
    }

}
