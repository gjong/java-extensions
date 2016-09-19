package com.jongsoft.lang.core;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

public class SomeTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void getWithString() {
        assertThat(Some.some("MyText").get(), equalTo("MyText"));
    }

    @Test
    public void containsWithString() {
        Value<String> myText = Some.some("MyText");

        assertThat(myText.contains("MyText"), equalTo(true));
        assertThat(myText.contains("NotMyText"), equalTo(false));
    }

    @Test
    public void containsWithNull() {
        Value<String> myText = Some.some(null);

        assertThat(myText.contains(null), equalTo(true));
        assertThat(myText.contains("NotMyText"), equalTo(false));
    }

    @Test
    public void isPresentWithString() {
        Value<String> myText = Some.some("MyText");

        assertThat(myText.isPresent(), equalTo(true));
    }

    @Test
    public void ifPresentWithString() {
        Value<String> myText = Some.some("MyText");

        StringBuilder ifPresent = new StringBuilder();
        myText
            .ifPresent(str -> ifPresent.append("Good"))
            .elseRun(() -> ifPresent.append("Bad"));

        assertThat(ifPresent.toString(), equalTo("Good"));
    }

    @Test
    public void ifPresentExceptionWithString() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(equalTo("Good"));

        StringBuilder response = new StringBuilder();
        Some.some("MyText")
                .ifPresent(() -> new Exception("Good"))
                .elseRun(() -> response.append("Bad"));

        assertThat(response.toString(), equalTo(""));
    }

    @Test
    public void ifNotPresentWithString() throws Exception {
        Value<String> myText = Some.some("MyText");

        StringBuilder ifNotPresent = new StringBuilder();
        myText.ifNotPresent(() -> ifNotPresent.append("Bad"));
        myText.ifNotPresent((Supplier<Exception>) Exception::new);

        assertThat(ifNotPresent.toString(), equalTo(""));
    }
}
