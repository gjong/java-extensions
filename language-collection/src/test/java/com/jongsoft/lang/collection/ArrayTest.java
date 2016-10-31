package com.jongsoft.lang.collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

public class ArrayTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void empty() {
        Array empty = Array.empty();

        assertThat(empty.size(), equalTo(0));
        assertThat(empty.iterator().hasNext(), equalTo(false));
    }

    @Test
    public void singleInt() {
        Array<Integer> ints = Array.of(5);

        assertThat(ints.size(), equalTo(1));
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
        List<Integer> insert = Array.of(5).insert(0, 2);

        assertThat(insert.size(), equalTo(2));
        assertThat(insert.get(0), equalTo(2));
        assertThat(insert.get(1), equalTo(5));
    }

    @Test
    public void insertAt1Int() {
        List<Integer> insert = Array.of(5).insert(1, 2);

        assertThat(insert.size(), equalTo(2));
        assertThat(insert.get(0), equalTo(5));
        assertThat(insert.get(1), equalTo(2));
    }

    @Test
    public void insertAt2Int() {
        List<Integer> insert = Array.of(1,2,3,4,5).insert(2, 12);

        assertThat(insert.size(), equalTo(6));
        assertThat(insert.get(0), equalTo(1));
        assertThat(insert.get(1), equalTo(2));
        assertThat(insert.get(2), equalTo(12));
        assertThat(insert.get(3), equalTo(3));
    }

    @Test
    public void addInt() {
        Array<Integer> source = Array.of(5);
        List<Integer> add = source.add(6);

        assertThat(source.size(), equalTo(1));
        assertThat(add.size(), equalTo(2));
        assertThat(add.get(0), equalTo(5));
        assertThat(add.get(1), equalTo(6));
    }

    @Test
    public void removeIntAt2() {
        List<Integer> removed = Array.of(1, 2, 3, 4, 5).remove(2);

        assertThat(removed.size(), equalTo(4));
        assertThat(removed.contains(2), equalTo(false));
    }

    @Test
    public void filterInt5() {
        List<Integer> noFives = Array.of(1, 5, 34, 4, 5, 23, 4, 5).filter(i -> 5 != i);

        assertThat(noFives.size(), equalTo(5));
        assertThat(noFives.get(0), equalTo(1));
        assertThat(noFives.get(1), equalTo(34));
        assertThat(noFives.get(2), equalTo(4));
        assertThat(noFives.get(3), equalTo(23));
    }

    @Test
    public void ofList() {
        Array<Integer> integers = Array.ofAll(Arrays.asList(0, 1, 2, 3, 4, 5));
        assertThat(integers.size(), equalTo(6));
    }

    @Test
    public void ofIterable() {
        Array<Integer> integers = Array.ofAll(() -> Collections.singleton(Integer.valueOf(5)).iterator());

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
}
