package com.jongsoft.lang;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

public class ValueTypeTest {

    @Test
    public void isSingleValued() {
        Assert.assertTrue(new ValueType<>("test").isSingleValued());
    }

    @Test
    public void get() {
        Assert.assertThat(new ValueType<>(1).get(), CoreMatchers.equalTo(1));
    }

    @Test
    public void filter_matching() {
        String result = new ValueType<>("test")
                .filter(str -> str.length() > 1)
                .get();

        Assert.assertEquals("test", result);
    }

    @Test(expected = NoSuchElementException.class)
    public void filter_not_matching() {
        new ValueType<>("test")
                .filter(str -> str.length() > 10)
                .get();
    }

    @Test
    public void map() {
        int length = new ValueType<>("test")
                .map(String::length)
                .get();

        Assert.assertEquals(4, length);
    }

    @Test
    public void iterator() {
        String value = new ValueType<>("test")
                .iterator()
                .next();

        Assert.assertEquals("test", value);
    }
}
