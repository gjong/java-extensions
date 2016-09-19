/*
 * The MIT License
 *
 * Copyright 2016 Jong Soft.
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
package com.jongsoft.lang.control;

import com.jongsoft.lang.core.Filterable;
import com.jongsoft.lang.core.Mappable;
import com.jongsoft.lang.core.OrElse;
import com.jongsoft.lang.core.Value;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Optional<T> extends Value<T>, Mappable<T>, Filterable<T>, OrElse {
    
    @Override
    <U> Optional<U> map(Function<T, U> mapper);

    @Override
    Optional<T> filter(Predicate<T> predicate);

    /**
     * This method will provide the entity contained within the {@link Optional}, in case no entity
     * is present the {@link Supplier} is called to create the else situation.
     *
     * @param supplier  the supplier to create an entity of none is contained in the {@link Optional}
     * @return          either the contained entity or the created one using the {@link Supplier}
     */
    T getOrSupply(Supplier<T> supplier);

    /**
     * This method will return the contained entity within the {@link Optional}, in case no entity
     * is present an exception will be thrown using the provided <code>exceptionSupplier</code>.
     *
     * @param exceptionSupplier     the {@link Supplier} used to create the {@link Throwable}
     * @param <X>                   the type of the Throwable that will be thrown
     * @return                      the entity contained within the {@link Optional}
     * @throws X                    in case of no entity
     * @throws NullPointerException in case the exceptionSupplier was null
     */
    <X extends Throwable> T getOrThrow(Supplier<X> exceptionSupplier) throws X;
    
    //----------------------------------------------------------------------------------------------
    //-- All static helper methods to instantiate the Optional class
    
    /**
     * Creates a new {@link Optional} representing the provided value.
     * <p>
     * This method will throw an {@link NullPointerException} when the provided value is <code>null</code>.
     *
     * @param <T>       the type of the value
     * @param value     the actual value
     *
     * @return          an {@link Optional} representing the value
     */
    static <T> Optional<T> of(T value) {
        Objects.requireNonNull(value, "Value cannot be null");
        return new OptionalSome<>(value);
    }
    
    /**
     * Creates a new {@link Optional} representing the provided value.
     * <p>
     * This method will allow <code>null</code> values.
     *
     * @param <T>       the type of the value
     * @param value     the actual value
     *
     * @return          an {@link Optional} representing the value
     */
    @SuppressWarnings("unchecked")
    static <T> Optional<T> ofNullable(T value) {
        return value != null 
                ? new OptionalSome<>(value) 
                : (Optional<T>) OptionalNone.NONE;
    }
    
    /**
     * Creates a default empty {@link Optional}.
     * 
     * @param <T>       the type of the value
     * @return          a default optional with a <code>null</code> value
     */
    @SuppressWarnings("unchecked")
    static <T> Optional<T> empty() {
        return (Optional<T>) OptionalNone.NONE;
    }

}
