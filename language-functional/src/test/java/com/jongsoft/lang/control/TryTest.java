package com.jongsoft.lang.control;

import com.jongsoft.lang.exception.NonFatalException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Objects;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

public class TryTest {
    
    @Rule
    public ExpectedException thrown = none();
    
    @Test
    public void trySupply() {
        Try<String> test = Try.supply(() -> "test");
        
        assertThat(test.isPresent(), equalTo(true));
        assertThat(test.isSuccess(), equalTo(true));
        assertThat(test.isFailure(), equalTo(false));
        assertThat(test.get(), equalTo("test"));
    }
    
    @Test
    public void trySupplyNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Supplier cannot be null");
        Try.supply(null);
    }
    
    @Test
    public void trySupplyAndConsume() {
        StringBuilder response = new StringBuilder();
        Try<String> test = Try.supply(() -> "test")
                .and(tst -> response.append(tst));
        
        assertThat(response.toString(), equalTo("test"));
        assertThat(test.isPresent(), equalTo(true));
        assertThat(test.isSuccess(), equalTo(true));
        assertThat(test.isFailure(), equalTo(false));
    }
    
    @Test
    public void trySupplyAndConsumeException() {
        Try<String> result = Try.supply(() -> "test")
                .and(tst -> {
                    throw new UnsupportedOperationException("big boobo");
                });
        
        assertThat(result.isFailure(), equalTo(true));
        assertThat(result.getCause(), instanceOf(UnsupportedOperationException.class));
        assertThat(result.getCause().getMessage(), equalTo("big boobo"));
    }
    
    @Test
    public void trySupplyAndConsumeNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Consumer cannot be null");
        Consumer<String> c = null;
        Try.supply(() -> "test")
                .and(c);
    }
    
    @Test
    public void trySupplyException() {
        thrown.expect(NonFatalException.class);
        
        Try<String> test = Try.supply(() -> {
            throw new UnsupportedOperationException();
        });
        
        assertThat(test.isPresent(), equalTo(false));
        assertThat(test.isSuccess(), equalTo(false));
        assertThat(test.isFailure(), equalTo(true));
        assertThat(test.getCause(), instanceOf(UnsupportedOperationException.class));
        
        test.get();
    }
    
    @Test
    public void tryRun() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = Try.run(() -> {
            response.append("test");
        });
        
        assertThat(response.toString(), equalTo("test"));
        assertThat(test.isPresent(), equalTo(true));
        assertThat(test.isSuccess(), equalTo(true));
        assertThat(test.isFailure(), equalTo(false));
    }
    
    @Test
    public void tryRunAndConsume() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = Try.run(() -> {
            response.append("test");
        }).and(d -> {
            assertThat(d, is(nullValue()));
        });
        
        assertThat(response.toString(), equalTo("test"));
        assertThat(test.isPresent(), equalTo(true));
        assertThat(test.isSuccess(), equalTo(true));
        assertThat(test.isFailure(), equalTo(false));
    }
    
    @Test
    public void tryRunAndRun() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = Try.run(() -> {
            response.append("test");
        }).and(() -> {
            response.append(" two");
        });
        
        assertThat(response.toString(), equalTo("test two"));
        assertThat(test.isPresent(), equalTo(true));
        assertThat(test.isSuccess(), equalTo(true));
        assertThat(test.isFailure(), equalTo(false));
    }
    
    @Test
    public void tryRunAndRunException() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = Try.run(() -> {
            response.append("test");
        }).and(() -> {
            throw new UnsupportedOperationException("Is good");
        });
        
        assertThat(response.toString(), equalTo("test"));
        assertThat(test.isPresent(), equalTo(false));
        assertThat(test.isSuccess(), equalTo(false));
        assertThat(test.isFailure(), equalTo(true));
        assertThat(test.getCause(), instanceOf(UnsupportedOperationException.class));
        assertThat(test.getCause().getMessage(), equalTo("Is good"));
    }
    
    @Test
    public void tryRunAndConsumeException() {
        StringBuilder response = new StringBuilder();
        Try<Void> test = Try.run(() -> {
            response.append("test");
        }).and(d -> {
            Objects.requireNonNull(d, "Was null");
        });
        
        assertThat(response.toString(), equalTo("test"));
        assertThat(test.isPresent(), equalTo(false));
        assertThat(test.isSuccess(), equalTo(false));
        assertThat(test.isFailure(), equalTo(true));
        assertThat(test.getCause(), instanceOf(NullPointerException.class));
        assertThat(test.getCause().getMessage(), equalTo("Was null"));
    }
    
    @Test
    public void tryRunException() {
        Try<Void> test = Try.run(() -> {
            throw new UnsupportedOperationException("Not implemented");
        });
        
        assertThat(test.isPresent(), equalTo(false));
        assertThat(test.isSuccess(), equalTo(false));
        assertThat(test.isFailure(), equalTo(true));
        assertThat(test.getCause(), instanceOf(UnsupportedOperationException.class));
        assertThat(test.getCause().getMessage(), equalTo("Not implemented"));
    }
    
}
