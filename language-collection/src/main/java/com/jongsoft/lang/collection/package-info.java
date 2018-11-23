/**
 * A purely functional based set of collection implementations based upon the {@linkplain com.jongsoft.lang.collection.Collection}.
 * <p>
 *     The implementation consists out of the following groups of interfaces:
 *     <ul>
 *         <li>{@linkplain com.jongsoft.lang.collection.Sequence}, a ordered collection type</li>
 *         <li>{@linkplain com.jongsoft.lang.collection.Set}, a collection type that ensures uniqueness</li>
 *         <li>{@linkplain com.jongsoft.lang.collection.ImmutableMap}, a map implementation</li>
 *         <li>{@linkplain com.jongsoft.lang.collection.Iterator}, a custom iteration implementation</li>
 *     </ul>
 * </p>
 * <p>
 *     All implementations within this set of collections are immutable. Meaning that all operations on the collections
 *     will result into either a new instance or an unchanged one. This leads to implementations similar to the snippet
 *     to add values to a Collection.
 * </p>
 * <pre>
 *     // Correct usage of an immutable collection
 *     Sequence&lt;String&gt; myStrings = Array.of("one")
 *              .add("two")
 *              .add("three");
 *
 *     // Incorrect usage of an immutable collection, as the initial collection assigned to myString does not change
 *     Sequence&lt;String&gt; myStrings = Array.of("one");
 *     myStrings.add("two");
 *     myStrings.add("three");
 * </pre>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Immutable_object">Immutable objects explaind</a>
 * @since 0.0.2
 * @author Gerben Jongerius
 */
package com.jongsoft.lang.collection;
