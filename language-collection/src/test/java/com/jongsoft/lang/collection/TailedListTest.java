package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TailedListTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void ofMultiple() {
        TailedList<String> list = TailedList.of("my", "test");

        assertThat(list.size(), equalTo(2));
        assertThat(list.get(0), equalTo("my"));
        assertThat(list.get(1), equalTo("test"));
    }

    @Test
    public void ofMultipleAdd() {
        TailedList<String> list = (TailedList<String>) TailedList.of("my", "test").add("added");

        assertThat(list.size(), equalTo(3));
        assertThat(list.get(0), equalTo("my"));
        assertThat(list.get(1), equalTo("test"));
        assertThat(list.get(2), equalTo("added"));
    }

    @Test
    public void indexOfString() {
        Sequence<String> list = TailedList.of("my", "string", "two");
        assertThat(list.indexOf("string"), equalTo(1));
        assertThat(list.indexOf("two"), equalTo(2));
        assertThat(list.indexOf("my"), equalTo(0));
        assertThat(list.indexOf("notIn"), equalTo(-1));
    }

    @Test
    public void getString() {
        Sequence<String> list = TailedList.of("first", "second", "third");

        assertThat(list.get(0), equalTo("first"));
        assertThat(list.get(1), equalTo("second"));
        assertThat(list.get(2), equalTo("third"));
    }

    @Test
    public void getOutOfBounds() {
        thrown.expect(IndexOutOfBoundsException.class);
        TailedList.of("first", "second", "third").get(3);
    }

    @Test
    public void remove() {
        final Sequence<String> result = TailedList.of("first", "second", "third")
                                                  .remove(1);

        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0), equalTo("first"));
        assertThat(result.get(1), equalTo("third"));
    }

    @Test
    public void removeOutOfBounds() {
        thrown.expect(IndexOutOfBoundsException.class);
        final Sequence<String> result = TailedList.of("first", "second", "third")
                                                  .remove(3);
    }

    @Test
    public void iterator() {
        final Iterator<String> listIterator = TailedList.of("first", "second", "third")
                                                        .iterator();

        assertThat(listIterator.hasNext(), equalTo(true));
        assertThat(listIterator.next(), equalTo("first"));
        assertThat(listIterator.hasNext(), equalTo(true));
        assertThat(listIterator.next(), equalTo("second"));
        assertThat(listIterator.hasNext(), equalTo(true));
        assertThat(listIterator.next(), equalTo("third"));
        assertThat(listIterator.hasNext(), equalTo(false));
    }

    @Test
    public void iteratorOutOfBounds() {
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(equalTo("No next element available in the iterator"));

        final Iterator<String> first = TailedList.of("first").iterator();
        first.next();
        first.next();
    }

    @Test
    public void ofAll() {
        final TailedList<String> ofAll = TailedList.ofAll(TailedList.of("one", "two"));

        assertThat(ofAll.get(0), equalTo("one"));
        assertThat(ofAll.get(1), equalTo("two"));
    }
    
    @Test
    public void map() {
        TailedList<Integer> mapped = TailedList.of("one", "three")
                .map(String::length);

        assertThat(mapped.size(), equalTo(2));
        assertThat(mapped.get(0), equalTo(3));
        assertThat(mapped.get(1), equalTo(5));
    }

}
