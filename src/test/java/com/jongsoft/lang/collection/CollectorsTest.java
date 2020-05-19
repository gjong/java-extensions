package com.jongsoft.lang.collection;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class CollectorsTest {

    @Test
    public void toList() {
        List<String> result = Arrays.asList("one", "two", "three")
              .stream()
              .collect(Collectors.toList());

        assertThat(result.size(), CoreMatchers.equalTo(3));
        assertThat(result, CoreMatchers.hasItems("one", "two", "three"));
    }

    @Test
    public void toLinkedList() {
        List<String> result = Arrays.asList("one", "two", "three")
                                        .stream()
                                        .collect(Collectors.toLinkedList());

        assertThat(result.size(), CoreMatchers.equalTo(3));
        assertThat(result, CoreMatchers.hasItems("one", "two", "three"));
    }

    @Test
    public void toSet() {
        Set<String> result = Arrays.asList("one", "two", "three", "two")
                                        .stream()
                                        .collect(Collectors.toSet());

        assertThat(result.size(), CoreMatchers.equalTo(3));
        assertThat(result, CoreMatchers.hasItems("one", "two", "three"));
    }

    @Test
    public void toSorted() {
        Set<String> result = Arrays.asList("one", "two", "three", "two")
                                   .stream()
                                   .collect(Collectors.toSorted(String::compareTo));

        assertThat(result.size(), CoreMatchers.equalTo(3));
        assertThat(result.get(0), CoreMatchers.equalTo("one"));
        assertThat(result.get(1), CoreMatchers.equalTo("three"));
        assertThat(result.get(2), CoreMatchers.equalTo("two"));
    }

}
