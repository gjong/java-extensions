package com.jongsoft.lang.collection;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

public class TailedList<T> implements List<T> {
    private static TailedList<?> EMPTY = new TailedList<>(null, null);

    private final T element;
    private final TailedList<T> tail;

    private TailedList(T element, TailedList<T> tail) {
        this.element = element;
        this.tail = tail;
    }

    @Override
    public List<T> add(T value) {
        return new TailedList<>(value, reverse()).reverse();
    }

    @Override
    public List<T> insert(int index, T value) {
        return null;
    }

    @Override
    public int indexOf(Object lookFor) {
        int index = 0;
        for (TailedList<T> list = this; !list.isEmpty(); list = list.tail, index++) {
            if (Objects.equals(list.element, lookFor)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        validateIndexOutOfBounds(index);

        int loopIdx = 0;
        TailedList<T> computed;
        for (computed = this; loopIdx < index; computed = computed.tail, loopIdx++);
        return computed.element;
    }

    @Override
    public List<T> remove(int index) {
        validateIndexOutOfBounds(index);

        TailedList<T> reversed = (TailedList<T>) TailedList.EMPTY;
        for (TailedList<T> newTail = this; !newTail.isEmpty(); newTail = newTail.tail, index--) {
            if (index != 0) {
                reversed.add(newTail.element);
            }
        }

        return reversed.reverse();
    }

    private TailedList<T> reverse() {
        TailedList<T> corrected = (TailedList<T>) TailedList.EMPTY;
        for (int i = 0; i < size(); i++) {
            corrected = new TailedList<>(get(i), corrected);
        }
        return corrected;
    }

    @Override
    public List<T> filter(Predicate<T> predicate) {
        return null;
    }

    @Override
    public int size() {
        int size = 0;
        for (TailedList<T> list = this; !list.isEmpty(); list = list.tail, size++);
        return size;
    }

    @Override
    public boolean isEmpty() {
        return EMPTY.equals(this);
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    private void validateIndexOutOfBounds(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException(format("%s is not in the bounds of 0 and %s", index, size()));
        }
    }

    //------------------------------------------------------------------
    //-- Static supporting methods

    public static <T> TailedList<T> of(T element) {
        return TailedList.of((T[]) new Object[]{element});
    }

    public static <T> TailedList<T> of(T...elements) {
        TailedList<T> reversed = (TailedList<T>) TailedList.EMPTY;
        for (T element : elements) {
            reversed = new TailedList<>(element, reversed);
        }

        return reversed.reverse();
    }
}
