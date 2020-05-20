package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.jongsoft.lang.API;

public class HashSetTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void append() {
        final Set<String> strings = API.<String>Set()
                .append("one")
                .append("two")
                .append("one");

        assertThat(strings.size(), equalTo(2));
        assertThat(strings, hasItems("one", "two"));
    }

    @Test
    public void union() {
        Set<Integer> result = API.Set(1, 2, 3)
           .union(API.Set(3, 4, 5));

        assertThat(result.size(), equalTo(5));
        assertThat(result, hasItems(1, 2, 3, 4, 5));
    }

    @Test
    public void intersect() {
        Set<Integer> result = API.Set(1, 2, 3)
           .intersect(API.Set(3, 4, 5));

        assertThat(result.size(), equalTo(1));
        assertThat(result, hasItem(3));
    }

    @Test
    public void intersectEmpty() {
        Set<Integer> result = API.Set(1, 2, 3)
                .intersect();

        assertThat(result.isEmpty(), equalTo(true));
        assertThat(result.size(), equalTo(0));
    }

    @Test
    public void intersectMultiple() {
        Set<Integer> result = API.Set(1, 2, 3)
           .intersect(API.List(3, 4, 5), API.List(4, 7, 3));

        assertThat(result.size(), equalTo(1));
        assertThat(result, hasItem(3));
    }

    @Test
    public void complement() {
        Set<Integer> result = API.Set(1, 2, 3, 4)
           .complement(API.Set(3, 2));

        assertThat(result.size(), equalTo(2));
        assertThat(result, hasItems(1, 4));
    }

    @Test
    public void complementMultiple() {
        Set<Integer> result = API.Set(1, 2, 3, 4)
           .complement(API.Set(2), API.Set(3));

        assertThat(result.size(), equalTo(2));
        assertThat(result, hasItems(1, 4));

        assertThat(API.Set(1, 2, 3).complement(), hasItems(1, 2, 3));
    }
    
    @Test
    public void toJava() {
        final java.util.Set<Integer> ints = API.Set(1, 2, 3, 4)
                                               .toJava();

        assertThat(ints.size(), equalTo(4));
        assertThat(ints, hasItems(1, 2, 3, 4));
    }

    @Test
    public void indexOf() {
        int index = API.<String>Set()
                .append("one")
                .append("two")
                .append("three")
                .indexOf("two");

        assertThat(index, equalTo(1));
    }

    @Test
    public void firstIndexOf() {
        Set<String> set = API.<String>Set()
                .append("one")
                .append("two")
                .append("three");

        int index = set.firstIndexWhere(str -> str.length() == 5);
        int noIndex = set.indexOf("five");

        assertThat(index, equalTo(2));
        assertThat(noIndex, equalTo(-1));
    }

    @Test
    public void get() {
        Set<String> strings = API.Set("one", "two", "one", "three");

        assertThat(strings.get(), equalTo("one"));
        assertThat(strings.get(0), equalTo("one"));
        assertThat(strings.get(1), equalTo("two"));
        assertThat(strings.get(2), equalTo("three"));
    }

    @Test
    public void getOutOfBounds() {
        thrown.expect(IndexOutOfBoundsException.class);

        API.Set("one").get(1);
        API.Set("one").get(-1);
    }

    @Test
    public void groupBy() {
        final Map<Integer, List<Integer>> pairs = API.Set(1, 2, 3, 4, 5).groupBy(x -> x % 2);

        assertThat(pairs.size(), equalTo(2));
        assertTrue(pairs.containsKey(0));
        assertThat(pairs.get(0).size(), equalTo(2));
        assertThat(pairs.get(0), hasItems(2, 4));
        assertTrue(pairs.containsKey(1));
        assertThat(pairs.get(1).size(), equalTo(3));
        assertThat(pairs.get(1), hasItems(1, 3, 5));
    }

    @Test
    public void headNoElement() {
        thrown.expect(NoSuchElementException.class);
        API.Set().head();
    }

    @Test
    public void head() {
        Integer head = API.Set(1, 2).head();
        assertThat(head, equalTo(1));
    }

    @Test
    public void tailNoElements() {
        thrown.expect(NoSuchElementException.class);
        API.Set().tail();
    }

    @Test
    public void tail() {
        assertThat(API.Set(1).tail().isEmpty(), equalTo(true));
        assertThat(API.Set(1, 1, 2).tail().head(), equalTo(2));
    }

    @Test
    public void remove() {
        Set<String> strings = API.Set("one", "two", "one", "three")
                .remove(2);

        assertThat(strings.size(), equalTo(2));
        assertThat(strings, hasItems("one", "two"));
    }

    @Test
    public void filter() {
        Set<String> filter = API.Set("one", "two", "one", "three")
                .filter(s -> s.length() > 3);

        assertThat(filter.size(), equalTo(1));
        assertThat(filter, hasItems(equalTo("three")));
    }

    @Test
    public void reject() {
        Set<Integer> noFives = API.Set(1, 2, 3, 4, 5)
                .reject(i -> i % 2 == 0);

        assertThat(noFives.size(), equalTo(3));
        assertThat(noFives, hasItems(1, 3, 5));
    }

    @Test
    public void map() {
        Set<Integer> lengths = API.Set("one", "two", "one", "three")
                .map(String::length);

        assertThat(lengths.size(), equalTo(2));
        assertThat(lengths, hasItems(3, 5));
    }

    @Test
    public void orElse() {
        Set<String> result = API.Set("test")
                .orElse(API.List("two"));

        Set<String> supplied = API.Set("test")
                .orElse(API.List("two"));

        assertThat(result, hasItem("test"));
        assertThat(supplied, hasItem("test"));
    }


    @Test
    public void fold() {
        String folded = API.Set("a", "b", "a", "c").fold("!", (xs, y) -> xs + y);
        String leftFolded = API.Set("a", "b", "a", "c").foldLeft("!", (xs, y) -> xs + y);

        assertThat(folded, equalTo("!abc"));
        assertThat(leftFolded, equalTo("!abc"));
    }

    @Test
    public void rightFold() {
        String folded = API.Set("a", "b", "a", "c").foldRight("!", (xs, y) -> xs + y);

        assertThat(folded, equalTo("cba!"));
    }

    @Test
    public void toStringTest() {
        Set<String> myStrings = API.Set("test-a", "test-b", "one")
                                        .filter(s -> s.length() == 6);

        assertThat(myStrings.toString(), equalTo("Set[test-a, test-b]"));
    }

    @Test
    public void equalTest() {
        assertEquals(API.Set(1, 2, 3), API.Set(1, 2, 3));
        assertEquals(API.Set("test", 2, 3), API.Set(2, "test", 3));
        assertNotEquals(API.Set("test", 2, 3), API.Set("test", 3));
    }

    @Test
    public void hashCodeTest() {
        assertEquals(21, API.Set().hashCode());
        assertEquals(22, API.Set(1).hashCode());
    }
}
