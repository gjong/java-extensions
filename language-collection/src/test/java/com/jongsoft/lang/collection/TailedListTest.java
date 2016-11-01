package com.jongsoft.lang.collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

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
        List<String> list = TailedList.of("my", "string", "two");
        assertThat(list.indexOf("string"), equalTo(1));
        assertThat(list.indexOf("two"), equalTo(2));
        assertThat(list.indexOf("my"), equalTo(0));
        assertThat(list.indexOf("notIn"), equalTo(-1));
    }

    @Test
    public void getString() {
        List<String> list = TailedList.of("first", "second", "third");

        assertThat(list.get(0), equalTo("first"));
        assertThat(list.get(1), equalTo("second"));
        assertThat(list.get(2), equalTo("third"));
    }

    @Test
    public void getOutOfBounds() {
        thrown.expect(IndexOutOfBoundsException.class);
        TailedList.of("first", "second", "third").get(3);
    }
}
