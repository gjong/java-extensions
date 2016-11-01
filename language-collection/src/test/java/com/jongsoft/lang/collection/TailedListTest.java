package com.jongsoft.lang.collection;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TailedListTest {

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
}
