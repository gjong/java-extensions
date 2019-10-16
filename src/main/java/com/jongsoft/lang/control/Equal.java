package com.jongsoft.lang.control;

/**
 * A control class to build an equality.
 */
public interface Equal {

    <T, R> Equal append(T left, R right);

    boolean isEqual();

}
