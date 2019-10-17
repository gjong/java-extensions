package com.jongsoft.lang.control;

import org.junit.Assert;
import org.junit.Test;
import com.jongsoft.lang.API;

public class EqualTest {

    @Test
    public void equal() {
        boolean equals = API.Equal(1, 1)
                .append("left", "left")
                .append(new String[] {"one", "two"}, new String[] {"one", "two"})
                .isEqual();

        Assert.assertTrue(equals);
    }

}
