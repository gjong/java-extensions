package com.jongsoft.lang.control;

@FunctionalInterface
public interface CheckedFunction<U,T> {

    U apply(T original);

}
