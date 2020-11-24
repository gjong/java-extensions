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

import static com.jongsoft.lang.API.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

import java.util.NoSuchElementException;

import com.jongsoft.lang.Control;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.jongsoft.lang.API;

public class OptionalTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void ofNullableWithNull() {
        assertThat(Control.Option(null).isPresent(), equalTo(false));
    }

    @Test
    public void ofNullableWithObject() {
        Optional<Integer> optional = Control.Option(Integer.MAX_VALUE);

        assertThat(optional.isPresent(), equalTo(true));
        assertThat(optional.get(), equalTo(Integer.MAX_VALUE));
    }

    @Test
    public void ifPresentWithException() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("Not present");

        Control.Option(null)
                .ifPresent(a -> Assert.fail())
                .elseThrow(() -> new Exception("Not present"));
    }

    @Test
    public void ifPresentWithRunnable() {
        StringBuilder response = new StringBuilder();
        Control.Option(null)
                .ifPresent(a -> Assert.fail())
                .elseRun(() -> response.append("ran"));

        assertThat(response.toString(), equalTo("ran"));
    }

    @Test
    public void ifPresentWithValue() {
        StringBuilder response = new StringBuilder();
        Control.Option("Good")
                .ifPresent(response::append)
                .elseRun(() -> response.append("Bad"));

        assertThat(response.toString(), equalTo("Good"));
    }

    @Test
    public void ifPresentWithValueThrow() {
        StringBuilder response = new StringBuilder();
        Control.Option("Good")
                .ifPresent(response::append)
                .elseThrow(IllegalStateException::new);

        assertThat(response.toString(), equalTo("Good"));
    }

    @Test
    public void ifPresentExceptionWithValue() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("Good");

        Control.Option("Good")
                .ifPresent(() -> new Exception("Good"));
    }

    @Test
    public void ifPresentExceptionWithoutValue() throws Exception {
        StringBuilder response = new StringBuilder();
        Control.Option(null)
                .ifPresent(() -> new Exception("Bad"))
                .elseRun(() -> response.append("Good"));

        assertThat(response.toString(), equalTo("Good"));
    }

    @Test
    public void ifNotPresent() {
        StringBuilder response = new StringBuilder();

        Control.Option(null)
                .ifNotPresent(() -> response.append("Good"));

        assertThat(response.toString(), equalTo("Good"));
    }

    @Test
    public void ifNotPresentException() throws Exception {
        thrown.expect(Exception.class);

        Control.Option(null)
                .ifNotPresent(() -> new Exception());
    }

    @Test
    public void mapWithElseThrow() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("Not present");

        Control.Option(null)
                .map(v1 -> {
                    Assert.fail("Should not map");
                    return "failed";
                })
                .getOrThrow(() -> new Exception("Not present"));
    }

    @Test
    public void getOrSupplyWithNullValue() {
        String result = Control.<String> Option(null)
                .getOrSupply(() -> "Test");

        assertThat(result, equalTo("Test"));
    }

    @Test
    public void getOrSupplyWithString() {
        String result = Control.Option("Good")
                .getOrSupply(() -> "Bad");

        assertThat(result, equalTo("Good"));
    }

    @Test
    public void getOrThrowString() throws Exception {
        String message = Control.Option("Good")
                .getOrThrow(Exception::new);

        assertThat(message, equalTo("Good"));
    }

    @Test
    public void getOrThrowNull() throws Exception {
        thrown.expect(Exception.class);

        Control.<String> Option(null)
                .getOrThrow(Exception::new);
    }

    @Test
    public void getWithNone() {
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(equalTo("No value is present"));

        boolean hasElement = Control.Option(null).iterator().hasNext();
        Control.Option(null).get();

        assertThat(hasElement, equalTo(false));
    }

    @Test
    public void map() {
        Optional<Integer> stringLength = Control.Option("My String")
                .map(String::length);

        assertThat(stringLength.get(), equalTo(9));
    }

    @Test
    public void filterSelf() {
        Optional<String> afterFilter = Control.Option("String")
                .filter(s -> s.equalsIgnoreCase("string"));

        assertThat(afterFilter.isPresent(), equalTo(true));
        assertThat(afterFilter.get(), equalTo("String"));
    }

    @Test
    public void filterNoResult() {
        Optional<String> afterFilter = Control.Option("String")
                .filter(s -> s.length() == 3);

        assertThat(afterFilter.isPresent(), equalTo(false));
    }

    @Test
    public void filterNone() {
        Optional<String> afterFilter = Control.<String> Option(null)
                .filter(s -> s.length() == 3);

        assertThat(afterFilter.isPresent(), equalTo(false));
    }

    @Test
    public void toStringSome() {
        String toString = Control.Option("String").toString();

        assertThat(toString, equalTo("Optional<Some>: String"));
    }

    @Test
    public void toStringNull() {
        String toString = Control.Option(null).toString();

        assertThat(toString, equalTo("Optional<Empty>: None"));
    }

    @Test
    public void isSingleValued() {
        assertThat(Control.Option(1).isSingleValued(), equalTo(true));
    }

}
