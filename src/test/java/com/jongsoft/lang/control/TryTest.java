package com.jongsoft.lang.control;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

import java.util.Objects;
import java.util.function.Consumer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.jongsoft.lang.API;
import com.jongsoft.lang.exception.NonFatalException;

public class TryTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void trySupply() {
        Try<String> test = API.Try(() -> "test");

        assertThat(test.isSuccess(), equalTo(true));
        assertThat(test.isFailure(), equalTo(false));
        assertThat(test.get(), equalTo("test"));
    }

    @Test
    public void trySupplyAndConsume() {
        StringBuilder response = new StringBuilder();
        Try<String> test = API.Try(() -> "test")
                .and(response::append);

        assertThat(response.toString(), equalTo("test"));
        assertThat(test.isSuccess(), equalTo(true));
        assertThat(test.isFailure(), equalTo(false));
    }

    @Test
    public void trySupplyAndConsumeException() {
        Try<String> result = API.Try(() -> "test")
                .and(tst -> {
                    throw new UnsupportedOperationException("big boobo");
                });

        assertThat(result.isFailure(), equalTo(true));
        assertThat(result.getCause(), instanceOf(UnsupportedOperationException.class));
        assertThat(result.getCause().getMessage(), equalTo("big boobo"));
    }

    @Test
    public void trySupplyAndConsumeExceptionRecover() {
        Try<String> result = API.Try(() -> "test")
                .and(tst -> {
                    throw new UnsupportedOperationException("big boobo");
                })
                .recover(x -> "auto");

        assertThat(result.isFailure(), equalTo(false));
        assertThat(result.get(), equalTo("auto"));
    }


    @Test
    public void trySupplyAndConsumeNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Consumer cannot be null");
        Consumer<String> c = null;
        API.Try(() -> "test")
                .and(c);
    }

    @Test
    public void trySupplyException() {
        thrown.expect(NonFatalException.class);

        Try<String> test = API.Try(() -> {
            throw new UnsupportedOperationException();
        });

        assertThat(test.isSuccess(), equalTo(false));
        assertThat(test.isFailure(), equalTo(true));
        assertThat(test.getCause(), instanceOf(UnsupportedOperationException.class));

        test.get();
    }

    @Test
    public void tryRun() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = API.Try(() -> {
            response.append("test");
        });

        assertThat(response.toString(), equalTo("test"));
        assertThat(test.isSuccess(), equalTo(true));
        assertThat(test.isFailure(), equalTo(false));
    }

    @Test
    public void tryRunSuccessGetCause() {
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage(equalTo("Cannot call getCause when Try is successful"));

        final Try<String> success = API.Try(() -> "My String");
        success.getCause();
    }

    @Test
    public void tryRunAndConsume() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = API.Try(() -> {
            response.append("test");
        }).and(d -> {
            assertThat(d, is(nullValue()));
        });

        assertThat(response.toString(), equalTo("test"));
        assertThat(test.isSuccess(), equalTo(true));
        assertThat(test.isFailure(), equalTo(false));
    }

    @Test
    public void tryRunAndRun() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = API.Try(() -> {
            response.append("test");
        }).and(() -> {
            response.append(" two");
        });

        assertThat(response.toString(), equalTo("test two"));
        assertThat(test.isSuccess(), equalTo(true));
        assertThat(test.isFailure(), equalTo(false));
    }

    @Test
    public void tryRunAndRunException() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = API.Try(() -> {
            response.append("test");
        }).and(() -> {
            throw new UnsupportedOperationException("Is good");
        });

        assertThat(response.toString(), equalTo("test"));
        assertThat(test.isSuccess(), equalTo(false));
        assertThat(test.isFailure(), equalTo(true));
        assertThat(test.getCause(), instanceOf(UnsupportedOperationException.class));
        assertThat(test.getCause().getMessage(), equalTo("Is good"));
    }

    @Test
    public void tryRunAndConsumeException() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = API.Try(() -> {
            response.append("test");
        }).and(d -> {
            Objects.requireNonNull(d, "Was null");
        });

        assertThat(response.toString(), equalTo("test"));
        assertThat(test.isSuccess(), equalTo(false));
        assertThat(test.isFailure(), equalTo(true));
        assertThat(test.getCause(), instanceOf(NullPointerException.class));
        assertThat(test.getCause().getMessage(), equalTo("Was null"));
    }

    @Test
    public void tryRunException() {
        Try<Void> test = API.Try(() -> {
            throw new UnsupportedOperationException("Not implemented");
        });

        assertThat(test.isSuccess(), equalTo(false));
        assertThat(test.isFailure(), equalTo(true));
        assertThat(test.getCause(), instanceOf(UnsupportedOperationException.class));
        assertThat(test.getCause().getMessage(), equalTo("Not implemented"));
    }

    @Test
    public void tryMap() {
        final Try<Integer> mapped = API.Try(() -> "My string")
                .map(String::length);

        assertThat(mapped.isFailure(), equalTo(false));
        assertThat(mapped.get(), equalTo(9));
    }

    @Test
    public void tryMapException() {
        final Try<Integer> mapped = API.Try(() -> "My string")
                                       .map(string -> {
                                           throw new UnsupportedOperationException("Not implemented");
                                       });

        assertThat(mapped.isFailure(), equalTo(true));
        assertThat(mapped.getCause(), instanceOf(UnsupportedOperationException.class));
        assertThat(mapped.getCause().getMessage(), equalTo("Not implemented"));
    }

}
