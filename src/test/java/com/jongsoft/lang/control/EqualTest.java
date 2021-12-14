package com.jongsoft.lang.control;

import com.jongsoft.lang.Control;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EqualTest {

    @Test
    void equal() {
        final Equal equal = Control.Equal(1, 1)
                               .append("left", "left")
                               .append(new String[]{ "one", "two" }, new String[]{ "one", "two" });

        assertThat(equal)
                .satisfies(Equal::isEqual)
                .hasToString("Equal[true]");
    }

    @Test
    void notEqual() {
        final Equal equal = Control.Equal(1, 1)
                               .append("left", "right")
                               .append(new String[]{ "one", "two" }, new String[]{ "one", "two", "three" });

        assertThat(equal)
                .satisfies(Equal::isNotEqual)
                .hasToString("Equal[false]");
    }

}
