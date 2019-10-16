package com.jongsoft.lang.control.impl;

import com.jongsoft.lang.control.Equal;

import java.util.Objects;

public class EqualHelper {

    public static final Equal IS_EQUAL = new Equal() {

        @Override
        public <T, R> Equal append(T left, R right) {
            if (Objects.equals(left, right)) {
                return this;
            }

            return NOT_EQUAL;
        }

        @Override
        public boolean isEqual() {
            return true;
        }

    };

    public static final Equal NOT_EQUAL = new Equal() {
        @Override
        public <T, R> Equal append(T left, R right) {
            return NOT_EQUAL;
        }

        @Override
        public boolean isEqual() {
            return false;
        }

    };

    private EqualHelper() {

    }

}
