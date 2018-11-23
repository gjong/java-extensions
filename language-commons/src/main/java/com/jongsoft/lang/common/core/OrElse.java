/*
 * The MIT License
 *
 * Copyright 2016-2018 Jong Soft.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.jongsoft.lang.common.core;

import java.util.function.Supplier;

import com.jongsoft.lang.common.Runner;

/**
 * The OrElse interface is an extension to the {@link Presence} interface. This interface can be used to cascade behaviour
 * when an entity is or is not present in the wrapper.
 * <p>
 *     For example if the {@link Presence} has no entity contained within then the OrElse can be used to throw an exception
 *     or run alternative logic. See the example below.
 * </p>
 * <pre>
 *     Optional.ofNullable(null)
 *      .ifPresent(System.out::println)
 *      .elseThrow(() -> new NullPointerException("No value present"))
 * </pre>
 *
 * @since 0.0.1
 */
public interface OrElse {

    /**
     * This operation will run when the precondition of this OrElse is not met.
     *
     * @param runner    the code to be run when the else is running
     */
    default void elseRun(Runner runner) {}

    /**
     * This operation will create an exception using the provided {@link Supplier} and throw it.
     *
     * @param exceptionSupplier the supplier to create the exception
     * @param <X>               the type of exception
     * @throws X                the exception in case of an OrElse run
     */
    default <X extends Throwable> void elseThrow(Supplier<X> exceptionSupplier) throws X {}

    //----------------------------------------------------------------------------------------------
    //-- All static helper methods to instantiate the OrElse class
    
    static OrElse empty() {
        return Constants.OR_ELSE_EMPTY;
    }
    
    static OrElse notEmpty() {
        return Constants.OR_ELSE_NOT_EMTPY;
    }
}
