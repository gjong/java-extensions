package com.jongsoft.lang.collection.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.stream.Collectors;

import com.jongsoft.lang.Collections;
import org.junit.Assert;
import org.junit.Test;
import com.jongsoft.lang.API;

public class PipeCommandTest {

    @Test
    public void consume() {
        StringBuilder consumed = new StringBuilder();

        new PipeCommand<>(Collections.List("text 1", "car", "plane"))
                .map(String::length)
                .consume(consumed::append);

        Assert.assertThat(consumed.toString(), equalTo("635"));;
    }

    @Test
    public void pipeline() {
        java.util.List<String> strings = Collections.List("car", "plane", "bike")
           .pipeline()
           .reject(s -> s.length() > 4)
           .stream().collect(Collectors.toList());

        Assert.assertThat(strings.size(), equalTo(2));
        Assert.assertThat(strings, hasItems("car", "bike"));
    }

    @Test
    public void foldLeft() {
        assertThat(Collections.List("t", "e", "s", "t").pipeline().foldLeft("!", (x, y) -> x + y), equalTo("!test"));
    }

    @Test
    public void foldRight() {
        assertThat(Collections.List("t", "e", "s", "t").pipeline().foldRight("!", (x, y) -> x + y), equalTo("tset!"));
    }

    @Test
    public void reduceLeft() {
        assertThat(Collections.List("t", "e", "s", "t").pipeline().reduceLeft((x ,y) -> x + y), equalTo("test"));
    }

}
