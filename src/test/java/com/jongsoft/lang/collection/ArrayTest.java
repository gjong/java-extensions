package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.jongsoft.lang.Collections;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.jongsoft.lang.API;
import com.jongsoft.lang.control.Optional;

public class ArrayTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void empty() {
        thrown.expect(NoSuchElementException.class);
        Sequence empty = Collections.List();

        empty.iterator().next();

        assertThat(empty.size(), equalTo(0));
        assertThat(empty.iterator().hasNext(), equalTo(false));
    }

    @Test
    public void pipeline() {
        Pipeline<Integer> pipe = Collections.List(1, 2, 3, 4)
                .pipeline()
                .map(x -> x * 2);

        Pipeline<Integer> pipe1 = pipe.filter(x -> x % 2 == 0);
        Pipeline<String> pipe2 = pipe.map(String::valueOf);

        Iterator<Integer> iterator = pipe1.iterator();
        assertThat(iterator.next(), equalTo(2));
        assertThat(iterator.next(), equalTo(4));
        assertThat(iterator.next(), equalTo(6));
        assertThat(iterator.next(), equalTo(8));
        assertFalse(iterator.hasNext());

        Iterator<String> iterator1 = pipe2.iterator();
        assertThat(iterator1.next(), equalTo("2"));
        assertThat(iterator1.next(), equalTo("4"));
        assertThat(iterator1.next(), equalTo("6"));
        assertThat(iterator1.next(), equalTo("8"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void headNoElement() {
        thrown.expect(NoSuchElementException.class);
        Collections.List().head();
    }

    @Test
    public void head() {
        Integer head = Collections.List(1, 2).head();
        assertThat(head, equalTo(1));
    }

    @Test
    public void tailNoElements() {
        thrown.expect(NoSuchElementException.class);
        Collections.List().tail();
    }

    @Test
    public void tail() {
        assertThat(Collections.List(1).tail().isEmpty(), equalTo(true));
        assertThat(Collections.List(1, 2).tail().head(), equalTo(2));
    }

    @Test
    public void singleInt() {
        Sequence<Integer> ints = Collections.List(5);

        assertThat(ints.size(), equalTo(1));
        assertThat(ints.isSingleValued(), equalTo(false));
        assertThat(ints.iterator().next(), equalTo(5));
    }

    @Test
    public void singleIntOutOfBounds() {
        thrown.expect(IndexOutOfBoundsException.class);
        thrown.expectMessage(equalTo("1 is not in the bounds of 0 and 1"));
        Collections.List(5).get(1);
    }
    
    @Test
    public void insertAt0Int() {
        Sequence<Integer> insert = Collections.List(5).insert(0, 2);

        assertThat(insert.size(), equalTo(2));
        assertThat(insert.get(0), equalTo(2));
        assertThat(insert.get(1), equalTo(5));
    }

    @Test
    public void insertAt1Int() {
        Sequence<Integer> insert = Collections.List(5).insert(1, 2);

        assertThat(insert.size(), equalTo(2));
        assertThat(insert.get(0), equalTo(5));
        assertThat(insert.get(1), equalTo(2));
    }

    @Test
    public void insertAt2Int() {
        Sequence<Integer> insert = Collections.List(1, 2, 3, 4, 5).insert(2, 12);

        assertThat(insert.size(), equalTo(6));
        assertThat(insert.get(0), equalTo(1));
        assertThat(insert.get(1), equalTo(2));
        assertThat(insert.get(2), equalTo(12));
        assertThat(insert.get(3), equalTo(3));
    }

    @Test
    public void append() {
        Sequence<Integer> source = Collections.List(5);
        Sequence<Integer> add = source.append(6);

        assertThat(source.size(), equalTo(1));
        assertThat(source.isEmpty(), equalTo(false));
        assertThat(add.size(), equalTo(2));
        assertThat(add.get(0), equalTo(5));
        assertThat(add.get(1), equalTo(6));
    }

    @Test
    public void prepend() {
        Sequence<Integer> result = Collections.List(2, 3, 4).prepend(1);

        assertThat(result.size(), equalTo(4));
        assertThat(result.get(0), equalTo(1));
        assertThat(result.get(1), equalTo(2));
        assertThat(result.get(2), equalTo(3));
        assertThat(result.get(3), equalTo(4));
    }
    
    @Test
    public void addAll() {
        Sequence<Integer> values = Collections.List(1, 2, 3)
                .union(Arrays.asList(4, 5, 6));

        assertThat(values.size(), equalTo(6));
        assertThat(values, hasItems(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void removeIntAt2() {
        Sequence<Integer> removed = Collections.List(1, 2, 3, 4, 5).remove(2);

        assertThat(removed.size(), equalTo(4));
        assertThat(removed.contains(3), equalTo(false));
    }

    @Test
    public void removeByElement() {
        Sequence<String> original = Collections.List("test", "string", "one");
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

        Collections.List(1, 2, 3, 4, 5).remove(6);
    }

    @Test
    public void filterInt5() {
        Sequence<Integer> noFives = Collections.List(1, 5, 34, 4, 5, 23, 4, 5)
                .filter(i -> 5 != i);

        assertThat(noFives.size(), equalTo(5));
        assertThat(noFives.get(0), equalTo(1));
        assertThat(noFives.get(1), equalTo(34));
        assertThat(noFives.get(2), equalTo(4));
        assertThat(noFives.get(3), equalTo(23));
    }

    @Test
    public void distinct() {
        Set<Integer> numbers = Collections.List(1, 2, 3, 1, 4, 2, 5)
           .distinct();

        assertThat(numbers.size(), equalTo(5));
        assertThat(numbers, hasItems(1, 2, 3, 4, 5));
    }
    
    @Test
    public void reject() {
        Sequence<Integer> noFives = Collections.List(1, 2, 3, 4, 5)
                .reject(i -> i % 2 == 0);

        assertThat(noFives.size(), equalTo(3));
        assertThat(noFives, hasItems(1, 3, 5));
    }

    @Test
    public void ofList() {
        Sequence<Integer> integers = Collections.List(Arrays.asList(0, 1, 2, 3, 4, 5));
        assertThat(integers.size(), equalTo(6));
    }

    @Test
    public void ofIterable() {
        Sequence<Integer> integers = Collections.List(() -> java.util.List.of(5).iterator());

        assertThat(integers.size(), equalTo(1));
        assertThat(integers.get(0), equalTo(5));
    }

    @Test
    public void containsNull() {
        assertThat(Collections.List(1,2,3,4,5,null).contains(null), equalTo(true));
        assertThat(Collections.List(1,2,3,4,5).contains(null), equalTo(false));
    }

    @Test
    public void contains5() {
        assertThat(Collections.List(1,2,3,4,5,null).contains(5), equalTo(true));
        assertThat(Collections.List(1,2,3,4).contains(5), equalTo(false));
    }

    @Test
    public void containsAll() {
        assertThat(Collections.List(1, 2, 3, 4).containsAll(Collections.List(2, 3)), equalTo(true));
    }

    @Test
    public void count() {
        int count = Collections.List(1, 2, 3, 4)
             .count(x -> x % 2 == 0);
        assertThat(count, equalTo(2));
    }

    @Test
    public void summing() {
        long total = Collections.List(1, 2, 3, 4)
                .summing(Long::sum);
        assertThat(total, equalTo(10L));
    }

    @Test
    public void fold() {
        assertThat(Collections.List("t", "e", "s", "t").fold("!", (x, y) -> x + y), equalTo("!test"));
        assertThat(Collections.List("t", "e", "s", "t").foldLeft("!", (x, y) -> x + y), equalTo("!test"));
    }

    @Test
    public void foldRight() {
        assertThat(Collections.List("t", "e", "s", "t").foldRight("!", (x, y) -> x + y), equalTo("tset!"));
    }

    @Test
    public void reduceLeft() {
        String reduce = Collections.List("t", "e", "s", "t").reduce((x ,y) -> x + y);
        String reduceLeft = Collections.List("t", "e", "s", "t").reduceLeft((x ,y) -> x + y);

        assertThat(reduce, equalTo("test"));
        assertThat(reduceLeft, equalTo("test"));
    }

    @Test
    public void indexOf5() {
        assertThat(Collections.List(1,2,3,4,5,null).indexOf(5), equalTo(4));
        assertThat(Collections.List(1,2,3,4).indexOf(5), equalTo(-1));
    }
    
    @Test
    public void map() {
        Sequence<Integer> mapped = Collections.List("test", "two")
                .map(String::length);

        assertThat(mapped.size(), equalTo(2));
        assertThat(mapped, hasItems(4, 3));
    }

    @Test
    public void orElse() {
        Sequence<String> result = Collections.List("test")
                .orElse(Collections.List("two"));

        Sequence<String> supplied = Collections.List("test")
                .orElse(Collections.List("two"));

        assertThat(result, hasItem("test"));
        assertThat(supplied, hasItem("test"));
    }

    @Test
    public void sum() {
        final Optional<Double> sum = Collections.List(1, 3, 3).sum();
        assertThat(sum.get(), equalTo(7.0));
        assertThat(Collections.List(1.0, 10e100, 2.0, -10e100).sum().get(), equalTo(3.0));
        assertTrue(Double.isNaN(Collections.List(1.0, Double.NaN).sum().get()));
    }

    @Test
    public void sum_empty() {
        final Optional<Double> sum = Collections.List().sum();
        assertThat(sum.isPresent(), equalTo(false));
    }

    @Test
    public void average() {
        assertThat(Collections.List().average().isPresent(), equalTo(false));
        assertThat(Collections.List(1, 2, 3).average().get(), equalTo(2.0));
        assertThat(Collections.List(1.0, 10e100, 2.0, -10e100).average().get(), equalTo(0.75));

        assertTrue(Double.isNaN(Collections.List(1.0, Double.NaN).average().get()));
    }

    @Test(expected = ClassCastException.class)
    public void average_NoNumber() {
        Collections.List("st", "tes").average();
    }

    @Test(expected = ClassCastException.class)
    public void sum_NoNumber() {
        Collections.List("st", "tes").sum();
    }

    @Test
    public void allNone() {
        Sequence<String> result = Collections.List("o", "n", "b");

        assertThat(result.none(x -> x.length() == 2), equalTo(true));
        assertThat(result.all(x -> x.length() == 1), equalTo(true));
    }

    @Test
    public void groupBy() {
        Map<Integer, List<String>> result = Collections.List("one", "two", "three", "four")
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
        final Sequence<Integer> array = Collections.List(1, 2, 3, 4, 5);

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
        final Sequence<Integer> result = Collections.List(1, 2, 3).reverse();

        assertThat(result.size(), equalTo(3));
        assertThat(result.get(0), equalTo(3));
        assertThat(result.get(1), equalTo(2));
        assertThat(result.get(2), equalTo(1));
    }

    @Test
    public void toJava() {
        java.util.List<Integer> integers = Collections.List(1, 2, 3, 4)
                .toJava();

        assertThat(integers, instanceOf(ArrayList.class));
        assertThat(integers.size(), equalTo(4));
        assertThat(integers, hasItems(1, 2, 3, 4));
    }

    @Test
    public void toStringTest() {
        Sequence<String> myStrings = Collections.List("test", "string", "one")
                                        .filter(s -> s.length() == 3)
                                        .remove("test");

        assertThat(myStrings.toString(), equalTo("Sequence[one]"));
    }

    @Test
    public void equalTest() {
        assertEquals(Collections.List(1, 2, 3), Collections.List(1, 2, 3));
        assertEquals(Collections.List("test", 2, 3), Collections.List(2, "test", 3));
        assertNotEquals(Collections.List("test", 2, 3), Collections.List("test", 3));
        assertNotEquals(Collections.List("test", 2, 3), Collections.List("test", 3, null));
    }

    @Test
    public void hashCodeTest() {
        assertEquals(19, Collections.List().hashCode());
        assertEquals(20, Collections.List(null, 1).hashCode());
    }
}
