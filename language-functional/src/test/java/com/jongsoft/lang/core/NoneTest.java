package com.jongsoft.lang.core;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NoneTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void get() {
        thrown.expect(NoSuchElementException.class);
        None.none().get();
    }

    @Test
    public void isPresent() {
        assertThat(None.none().isPresent(), equalTo(false));
    }

    @Test
    public void ifPresent() {
        StringBuilder ifPresent = new StringBuilder();
        None.none()
                .ifPresent(str -> ifPresent.append("Bad"))
                .elseRun(() -> ifPresent.append("Good"));

        assertThat(ifPresent.toString(), equalTo("Good"));
    }

    @Test
    public void ifPresentException() throws Exception {
        StringBuilder ifPresent = new StringBuilder();
        None.none()
                .ifPresent(() -> new Exception())
                .elseRun(() -> ifPresent.append("Good"));

        assertThat(ifPresent.toString(), equalTo("Good"));
    }

    @Test
    public void ifNotPresent() {
        StringBuilder ifNotPresent = new StringBuilder();
        None.none()
            .ifNotPresent(() -> ifNotPresent.append("Good"));
        assertThat(ifNotPresent.toString(), equalTo("Good"));
    }

    @Test
    public void ifNotPresentException() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(equalTo("Not present"));

        None.none()
            .ifNotPresent(() -> new Exception("Not present"));
    }

    @Test
    public void tostring() {
        assertThat(None.none().toString(), equalTo("None"));
    }

    @Test
    public void iterator() {
        assertThat(None.none().iterator().hasNext(), equalTo(false));
    }
}
