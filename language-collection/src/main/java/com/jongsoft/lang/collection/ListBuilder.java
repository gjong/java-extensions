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
package com.jongsoft.lang.collection;

import java.util.stream.Stream;

import com.jongsoft.lang.common.Builder;

public class ListBuilder<T> {
    private Sequence<Builder<T>> builders = Array.empty();

    public ListBuilder<T> append(Builder<T> builder) {
        builders.add(builder);
        return this;
    }

    /**
     * Create a list of all builders that where added. See {@link #toStream()} for the specifics of the implementation.
     *
     * @return returns a list of the elements after the {@link Builder#build()} is called for each element.
     */
    public Collection<T> toList() {
        return toStream().collect(Array.collector());
    }

    public Stream<T> toStream() {
        return builders.stream()
                .map(Builder::build);
    }

}
