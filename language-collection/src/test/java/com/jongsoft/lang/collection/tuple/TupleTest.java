package com.jongsoft.lang.collection.tuple;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TupleTest {

    @Test
    public void pairInts() {
        Tuple.Pair<Integer, Integer> pair = Tuple.Pair.of(1, 3);

        assertThat(pair.getFirst(), equalTo(1));
        assertThat(pair.getSecond(), equalTo(3));
    }

    @Test
    public void pairMixed() {
        Tuple.Pair<Integer, String> pair = Tuple.Pair.of(1, "test");

        assertThat(pair.getFirst(), equalTo(1));
        assertThat(pair.getSecond(), equalTo("test"));
    }

    @Test
    public void triplet() {
        Tuple.Triplet<String, Float, Integer> triplet = Tuple.Triplet.of("test", new Float(2.3), 5);

        assertThat(triplet.getFirst(), equalTo("test"));
        assertThat(triplet.getSecond(), equalTo(2.3f));
        assertThat(triplet.getThird(), equalTo(5));
    }

    @Test
    public void quadruplet() {
        Tuple.Quadruplet<Float, Integer, String, String> of = Tuple.Quadruplet.of(1f, 2, "test", "cat");

        assertThat(of.getFirst(), equalTo(1f));
        assertThat(of.getSecond(), equalTo(2));
        assertThat(of.getThird(), equalTo("test"));
        assertThat(of.getFourth(), equalTo("cat"));
    }
}
