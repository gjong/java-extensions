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
    public void add() {
        final Set<String> strings = API.<String>Set()
                .add("one")
                .add("two")
                .add("one");

        assertThat(strings.size(), equalTo(2));
        assertThat(strings, hasItems("one", "two"));
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
                .add("one")
                .add("two")
                .add("three")
                .indexOf("two");

        assertThat(index, equalTo(1));
    }

    @Test
    public void firstIndexOf() {
        Set<String> set = API.<String>Set()
                .add("one")
                .add("two")
                .add("three");

        int index = set.firstIndexOf(str -> str.length() == 5);
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
}
