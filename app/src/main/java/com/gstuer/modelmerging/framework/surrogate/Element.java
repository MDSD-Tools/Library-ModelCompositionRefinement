package com.gstuer.modelmerging.framework.surrogate;

import java.util.Objects;

public class Element<T> extends Replaceable {
    private final T value;

    public Element(T value, boolean isPlaceholder) {
        super(isPlaceholder);
        this.value = Objects.requireNonNull(value);
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Element<?> element = (Element<?>) object;
        return value.equals(element.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
