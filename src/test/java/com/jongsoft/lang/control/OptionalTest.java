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

import com.jongsoft.lang.Control;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalTest {

    @Test
    void ofNullableWithNull() {
        assertThat(Control.Option(null).isPresent()).isFalse();
    }

    @Test
    void ofNullableWithObject() {
        Optional<Integer> optional = Control.Option(Integer.MAX_VALUE);

        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void ifPresentWithException() {
        assertThatThrownBy(() -> Control.Option(null)
                .ifPresent(a -> Assertions.fail())
                .elseThrow(() -> new Exception("Not present")))
                .isInstanceOf(Exception.class)
                .hasMessage("Not present");
    }

    @Test
    void ifPresentWithRunnable() {
        StringBuilder response = new StringBuilder();
        Control.Option(null)
                .ifPresent(a -> Assertions.fail())
                .elseRun(() -> response.append("ran"));

        assertThat(response).hasToString("ran");
    }

    @Test
    void ifPresentWithValue() {
        StringBuilder response = new StringBuilder();
        Control.Option("Good")
                .ifPresent(response::append)
                .elseRun(() -> response.append("Bad"));

        assertThat(response).hasToString("Good");
    }

    @Test
    void ifPresentWithValueThrow() {
        StringBuilder response = new StringBuilder();
        Control.Option("Good")
                .ifPresent(response::append)
                .elseThrow(IllegalStateException::new);

        assertThat(response).hasToString("Good");
    }

    @Test
    void ifPresentExceptionWithValue() {
        assertThatThrownBy(() -> Control.Option("Good").ifPresent(() -> new Exception("Good")))
                .isInstanceOf(Exception.class)
                .hasMessage("Good");
    }

    @Test
    void ifPresentExceptionWithoutValue() throws Exception {
        StringBuilder response = new StringBuilder();
        Control.Option(null)
                .ifPresent(() -> new Exception("Bad"))
                .elseRun(() -> response.append("Good"));

        assertThat(response).hasToString("Good");
    }

    @Test
    public void ifNotPresent() {
        StringBuilder response = new StringBuilder();

        Control.Option(null)
                .ifNotPresent(() -> response.append("Good"));

        assertThat(response).hasToString("Good");
    }

    @Test
    public void ifNotPresentException() throws Exception {
        assertThatThrownBy(() -> Control.Option(null).ifNotPresent(() -> new Exception()))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void mapWithElseThrow() {
        assertThatThrownBy(() ->
                Control.Option(null)
                        .map(v1 -> {
                            Assertions.fail("Should not map");
                            return "failed";
                        })
                        .getOrThrow(() -> new Exception("Not present")))
                .isInstanceOf(Exception.class)
                .hasMessage("Not present");
    }

    @Test
    public void getOrSupplyWithNullValue() {
        String result = Control.<String>Option(null)
                .getOrSupply(() -> "Test");

        assertThat(result).isEqualTo("Test");
    }

    @Test
    public void getOrSupplyWithString() {
        String result = Control.Option("Good")
                .getOrSupply(() -> "Bad");

        assertThat(result).isEqualTo("Good");
    }

    @Test
    public void getOrThrowString() throws Exception {
        String message = Control.Option("Good")
                .getOrThrow(Exception::new);

        assertThat(message).isEqualTo("Good");
    }

    @Test
    public void getOrThrowNull() {
        assertThatThrownBy(() ->
                Control.<String>Option(null)
                        .getOrThrow(Exception::new))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void getWithNone() {
        boolean hasElement = Control.Option(null).iterator().hasNext();

        assertThatThrownBy(() -> Control.Option(null).get())
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No value is present");

        assertThat(hasElement).isFalse();
    }

    @Test
    public void map() {
        Optional<Integer> stringLength = Control.Option("My String")
                .map(String::length);

        assertThat(stringLength.get()).isEqualTo(9);
    }

    @Test
    public void filterSelf() {
        Optional<String> afterFilter = Control.Option("String")
                .filter(s -> s.equalsIgnoreCase("string"));

        assertThat(afterFilter.isPresent()).isTrue();
        assertThat(afterFilter.get()).isEqualTo("String");
    }

    @Test
    public void filterNoResult() {
        Optional<String> afterFilter = Control.Option("String")
                .filter(s -> s.length() == 3);

        assertThat(afterFilter.isPresent()).isFalse();
    }

    @Test
    public void filterNone() {
        Optional<String> afterFilter = Control.<String>Option(null)
                .filter(s -> s.length() == 3);

        assertThat(afterFilter.isPresent()).isFalse();
    }

    @Test
    public void toStringSome() {
        String toString = Control.Option("String").toString();

        assertThat(toString).isEqualTo("Optional<Some>: String");
    }

    @Test
    public void toStringNull() {
        String toString = Control.Option(null).toString();

        assertThat(toString).isEqualTo("Optional<Empty>: None");
    }

    @Test
    public void isSingleValued() {
        assertThat(Control.Option(1).isSingleValued()).isTrue();
    }

}
