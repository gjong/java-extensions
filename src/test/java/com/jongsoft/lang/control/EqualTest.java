package com.jongsoft.lang.control;

import com.jongsoft.lang.API;
import org.junit.Assert;
import org.junit.Test;

public class EqualTest {

    @Test
    public void equal() {
        boolean equals = API.Equal(1, 1)
                .append("left", "left")
                .isEqual();

        Assert.assertTrue(equals);
    }

}
