package com.jongsoft.lang.common.control;

@FunctionalInterface
public interface CheckedFunction<U,T> {

    U apply(T original);

}
