package com.jongsoft.lang.collection.support;

import com.jongsoft.lang.collection.Collection;
import com.jongsoft.lang.collection.Pipeline;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PipeCommand<T> implements Pipeline<T> {

    private final Supplier<Collection<T>> command;

    public PipeCommand(Collection<T> origin) {
        this.command = () -> origin;
    }

    private PipeCommand(Supplier<Collection<T>> command) {
        this.command = command;
    }

    @Override
    public <U> Pipeline<U> map(Function<T, U> mapper) {
        return new PipeCommand<>(() -> command.get().map(mapper));
    }

    @Override
    public Pipeline<T> filter(Predicate<T> predicate) {
        return new PipeCommand<>(() -> command.get().filter(predicate));
    }

    @Override
    public Pipeline<T> reject(Predicate<T> predicate) {
        return new PipeCommand<>(() -> command.get().reject(predicate));
    }

    @Override
    public Stream<T> stream() {
        return command.get().stream();
    }

    @Override
    public Iterator<T> iterator() {
        return command.get().iterator();
    }

    @Override
    public <U> U foldLeft(U start, BiFunction<? super U, ? super T, ? extends U> combiner) {
        return command.get().foldLeft(start, combiner);
    }

    @Override
    public <U> U foldRight(U start, BiFunction<? super T, ? super U, ? extends U> combiner) {
        return command.get().foldRight(start, combiner);
    }

    @Override
    public T reduceLeft(BiFunction<? super T, ? super T, ? extends T> reducer) {
        return command.get().reduceLeft(reducer);
    }
}
