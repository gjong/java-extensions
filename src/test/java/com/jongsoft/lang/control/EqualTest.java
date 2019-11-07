package com.jongsoft.lang.control;

import org.junit.Assert;
import org.junit.Test;
import com.jongsoft.lang.API;

public class EqualTest {

    @Test
    public void equal() {
        final Equal equal = API.Equal(1, 1)
                               .append("left", "left")
                               .append(new String[]{ "one", "two" }, new String[]{ "one", "two" });

        Assert.assertTrue(equal.isEqual());
        Assert.assertFalse(equal.isNotEqual());
        Assert.assertEquals("Equal[true]", equal.toString());
    }

    @Test
    public void notEqual() {
        final Equal equal = API.Equal(1, 1)
                               .append("left", "right")
                               .append(new String[]{ "one", "two" }, new String[]{ "one", "two", "three" });

        Assert.assertTrue(equal.isNotEqual());
        Assert.assertFalse(equal.isEqual());
        Assert.assertEquals("Equal[false]", equal.toString());
    }

}
