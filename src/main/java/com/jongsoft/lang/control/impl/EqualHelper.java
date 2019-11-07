package com.jongsoft.lang.control.impl;

import java.util.Objects;

import com.jongsoft.lang.control.Equal;

public class EqualHelper {

    public static final Equal IS_EQUAL = new Equal() {

        @Override
        public <T, R> Equal append(T left, R right) {
            boolean isEqual = false;
            if (left instanceof Object[] && right instanceof Object[]) {
                 isEqual = primitiveArrayEqual((Object[]) left, (Object[]) right);
            } else {
                isEqual = Objects.equals(left, right);
            }

            if (isEqual) {
                return this;
            }

            return NOT_EQUAL;
        }

        @Override
        public boolean isEqual() {
            return true;
        }

        @Override
        public boolean isNotEqual() {
            return false;
        }

        @Override
        public String toString() {
            return "Equal[true]";
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

        @Override
        public boolean isNotEqual() {
            return true;
        }

        @Override
        public String toString() {
            return "Equal[false]";
        }

    };

    private EqualHelper() {
        // helper class should not have an public constructor
    }

    private static boolean primitiveArrayEqual(Object[] left, Object[] right) {
        if (left.length != right.length) {
            return false;
        }

        for (int i = 0; i < left.length; i++) {
            if (IS_EQUAL.append(left[i], right[i]).isNotEqual()) {
                return false;
            }
        }

        return true;
    }

}
