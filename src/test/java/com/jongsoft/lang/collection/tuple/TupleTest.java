package com.jongsoft.lang.collection.tuple;

import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.Sequence;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TupleTest {

    @Test
    void pairInts() {
        Pair<Integer, Integer> pair = API.Tuple(1, 3);
        final Sequence actual = pair.toList();

        assertThat(pair.getFirst()).isEqualTo(1);
        assertThat(pair.getSecond()).isEqualTo(3);
        assertThat(pair).hasToString("Pair(1, 3)");
        assertThat(actual).hasSize(2);
    }

    @Test
    void pairMixed() {
        Pair<Integer, String> pair = API.Tuple(1, "test");

        assertThat(pair.getFirst()).isEqualTo(1);
        assertThat(pair.getSecond()).isEqualTo("test");
        assertThat(pair).hasToString("Pair(1, test)");
    }

    @Test
    void triplet() {
        Triplet<String, Float, Integer> triplet = API.Tuple("test", 2.3f, 5);

        assertThat(triplet.getFirst()).isEqualTo("test");
        assertThat(triplet.getSecond()).isEqualTo(2.3f);
        assertThat(triplet.getThird()).isEqualTo(5);
        assertThat(triplet).hasToString("Triplet(test, 2.3, 5)");
    }

    @Test
    void quadruplet() {
        Quadruplet<Float, Integer, String, String> of = API.Tuple(1f, 2, "test", "cat");

        assertThat(of.getFirst()).isEqualTo(1f);
        assertThat(of.getSecond()).isEqualTo(2);
        assertThat(of.getThird()).isEqualTo("test");
        assertThat(of.getFourth()).isEqualTo("cat");
        assertThat(of.toString()).isEqualTo("Quadruplet(1.0, 2, test, cat)");
    }
}
