package com.jongsoft.lang.common.core;

import java.util.function.Supplier;

import com.jongsoft.lang.common.Runner;

/**
 * This class contains utility methods and static classes to support the interfaces in this package.
 */
class Constants {
    static OrElse OR_ELSE_EMPTY = new OrElse() {
        @Override
        public void elseRun(Runner runner) {
            runner.run();
        }

        @Override
        public <X extends Throwable> void elseThrow(Supplier<X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }
    };

    static OrElse OR_ELSE_NOT_EMTPY = new OrElse() {};
}
