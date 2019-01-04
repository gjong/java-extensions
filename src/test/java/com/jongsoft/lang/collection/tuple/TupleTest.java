package com.jongsoft.lang.collection.tuple;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import com.jongsoft.lang.collection.Sequence;

public class TupleTest {

    @Test
    public void pairInts() {
        Tuple.Pair<Integer, Integer> pair = Tuple.Pair.of(1, 3);
        final Sequence actual = pair.toList();

        assertThat(pair.getFirst(), equalTo(1));
        assertThat(pair.getSecond(), equalTo(3));
        assertThat(pair.toString(), equalTo("Pair(1, 3)"));
        assertThat(actual.size(), equalTo(2));
    }

    @Test
    public void pairMixed() {
        Tuple.Pair<Integer, String> pair = Tuple.Pair.of(1, "test");

        assertThat(pair.getFirst(), equalTo(1));
        assertThat(pair.getSecond(), equalTo("test"));
        assertThat(pair.toString(), equalTo("Pair(1, test)"));
    }

    @Test
    public void triplet() {
        Tuple.Triplet<String, Float, Integer> triplet = Tuple.Triplet.of("test", 2.3f, 5);

        assertThat(triplet.getFirst(), equalTo("test"));
        assertThat(triplet.getSecond(), equalTo(2.3f));
        assertThat(triplet.getThird(), equalTo(5));
        assertThat(triplet.toString(), equalTo("Triplet(test, 2.3, 5)"));
    }

    @Test
    public void quadruplet() {
        Tuple.Quadruplet<Float, Integer, String, String> of = Tuple.Quadruplet.of(1f, 2, "test", "cat");

        assertThat(of.getFirst(), equalTo(1f));
        assertThat(of.getSecond(), equalTo(2));
        assertThat(of.getThird(), equalTo("test"));
        assertThat(of.getFourth(), equalTo("cat"));
        assertThat(of.toString(), equalTo("Quadruplet(1.0, 2, test, cat)"));
    }
}
