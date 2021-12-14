package com.jongsoft.lang.control;

import com.jongsoft.lang.Control;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TryTest {

    @Test
    public void trySupply() {
        Try<String> test = Control.Try(() -> "test");

        assertThat(test.isSuccess()).isTrue();
        assertThat(test.isFailure()).isFalse();
        assertThat(test.get()).isEqualTo("test");
    }

    @Test
    void trySupplyAndConsume() {
        StringBuilder response = new StringBuilder();
        Try<String> test = Control.Try(() -> "test")
                .consume(response::append);

        assertThat(response.toString()).isEqualTo("test");
    }

    @Test
    void trySupplyAndConsumeException() {
        Try<String> result = Control.Try(() -> "test")
                .consume(tst -> {
                    throw new UnsupportedOperationException("big boobo");
                });

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(result.getCause().getMessage()).isEqualTo("big boobo");
    }

    @Test
    void trySupplyAndConsumeExceptionRecover() {
        Try<String> result = Control.Try(() -> "test")
                .consume(tst -> {
                    throw new UnsupportedOperationException("big boobo");
                })
                .recover(x -> "auto");

        assertThat(result.isFailure()).isFalse();
        assertThat(result.get()).isEqualTo("auto");
    }


    @Test
    void trySupplyAndConsumeNull() {
        CheckedConsumer<String> c = null;

        assertThatThrownBy(() ->
                Control.Try(() -> "test")
                        .consume(c))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Consumer cannot be null");
    }

    @Test
    void trySupplyException() {
        Try<String> test = Control.Try(() -> {
            throw new UnsupportedOperationException();
        }).map(s -> s + "12");

        assertThat(test.isSuccess()).isFalse();
        assertThat(test.isFailure()).isTrue();
        assertThat(test.getCause()).isInstanceOf(UnsupportedOperationException.class);

        assertThatThrownBy(test::get);
    }

    @Test
    void tryRun() {
        StringBuilder response = new StringBuilder();
        Control.Try(() -> {
            response.append("test");
        });

        assertThat(response.toString()).isEqualTo("test");
    }

    @Test
    void tryRunSuccessGetCause() {
        final Try<String> success = Control.Try(() -> "My String");

        assertThatThrownBy(success::getCause)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Cannot call getCause when Try is successful");
    }

    @Test
    void tryRunAndConsume() {
        StringBuilder response = new StringBuilder();
        Control.Try(() -> {
            response.append("test");
        }).consume(d -> assertThat(d).isNull());

        assertThat(response.toString()).isEqualTo("test");
    }

    @Test
    void tryRunAndRun() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = Control.Try(() -> {
                    response.append("test");
                })
                .run(() -> response.append(" two"))
                .recover(th -> {
                    response.append("recovered");
                    return null;
                });

        assertThat(response).hasToString("test two");
        assertThat(test.isSuccess()).isTrue();
    }

    @Test
    void tryRunAndRunException() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = Control.Try(() -> {
            response.append("test");
        }).run(() -> {
            throw new UnsupportedOperationException("Is good");
        });

        assertThat(response).hasToString("test");
        assertThat(test.isSuccess()).isFalse();
        assertThat(test.getCause())
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Is good");
    }

    @Test
    void tryRunAndConsumeException() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = Control.Try(() -> {
            response.append("test");
        }).consume(d -> {
            Objects.requireNonNull(d, "Was null");
        });

        assertThat(response).hasToString("test");
        assertThat(test.isSuccess()).isFalse();
        assertThat(test.getCause())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Was null");
    }

    @Test
    void tryRunException() {
        Try<Void> test = Control.Try(() -> {
            throw new UnsupportedOperationException("Not implemented");
        });

        assertThat(test.isSuccess()).isFalse();
        assertThat(test.isFailure()).isTrue();
        assertThat(test.getCause())
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Not implemented");
    }

    @Test
    void tryMap() {
        final Try<Integer> mapped = Control.Try(() -> "My string")
                .map(String::length);

        assertThat(mapped.isFailure()).isFalse();
        assertThat(mapped.get()).isEqualTo(9);
    }

    @Test
    void tryMapException() {
        final Try<Integer> mapped = Control.Try(() -> "My string")
                .map(string -> {
                    throw new UnsupportedOperationException("Not implemented");
                });

        assertThat(mapped.isFailure()).isTrue();
        assertThat(mapped.getCause())
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Not implemented");
    }

}
