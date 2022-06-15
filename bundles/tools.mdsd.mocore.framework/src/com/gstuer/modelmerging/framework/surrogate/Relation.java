package com.gstuer.modelmerging.framework.surrogate;

import java.util.Objects;

public abstract class Relation<T extends Replaceable, S extends Replaceable> extends Replaceable {
    private final T source;
    private final S destination;

    protected Relation(T source, S destination, boolean isPlaceholder) {
        super(isPlaceholder);
        this.source = Objects.requireNonNull(source);
        this.destination = Objects.requireNonNull(destination);
    }

    public T getSource() {
        return source;
    }

    public S getDestination() {
        return destination;
    }

    @Override
    public boolean canReplace(Replaceable replaceable) {
        return this.source.canReplace(replaceable) || this.destination.canReplace(replaceable);
    }

    @SuppressWarnings("unchecked")
    protected <U extends Replaceable> T getSourceReplacement(U original, U replacement) {
        if (this.source.canReplace(original)) {
            // Type-safe cast due to possible replacement of source with original type.
            return (T) replacement;
        }
        return this.source;
    }

    @SuppressWarnings("unchecked")
    protected <U extends Replaceable> S getDestinationReplacement(U original, U replacement) {
        if (this.destination.canReplace(original)) {
            // Type-safe cast due to possible replacement of source with original type.
            return (S) replacement;
        }
        return this.destination;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Relation<?, ?> relation = (Relation<?, ?>) object;
        return source.equals(relation.source) && destination.equals(relation.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination);
    }
}
