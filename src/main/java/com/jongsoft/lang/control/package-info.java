/**
 * The control package contains flow control interfaces. These can be used to direct the logical flow of your code easier.
 *
 * <p><strong>Current support control mechanisms</strong></p>
 * <ul>
 *     <li>{@linkplain com.jongsoft.lang.control.Optional}, for {@code null} safe access to elements</li>
 *     <li>{@linkplain com.jongsoft.lang.control.Try}, for exception handling logic</li>
 *     <li>{@linkplain com.jongsoft.lang.control.Equal}, for logical equality building</li>
 * </ul>
 *
 * <p>
 *     The Optional is a functional representation of a value, or {@code null}. In both cases the Optional itself
 *     will be non null and allow functional access to its contents. It allows for handling when a value is present as
 *     well as supporting control operations in case no value is present.
 * </p>
 * <pre> {@code    // Sample usage of the Optional
 *     Control.Option("one")
 *          .ifPresent(System.out::println)
 *          .orElse(() -> System.out.println("No value is present");
 * }</pre>
 *
 * @since 0.0.1
 */
package com.jongsoft.lang.control;
