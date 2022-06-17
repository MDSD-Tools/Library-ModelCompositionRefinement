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
        return this.equals(replaceable) || this.source.canReplace(replaceable)
                || this.destination.canReplace(replaceable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends Replaceable> Relation<T, S> replace(U original, U replacement) {
        if (this.equals(original)) {
            // Type-safe cast due to this.equals(original) == true
            // => this.getClass() == original.getClass() == replacement.getClass()
            return (Relation<T, S>) replacement;
        } else if (original.isPlaceholder() || replacement.isPlaceholder()) {
            return this.replace(original, replacement, true);
        }
        return this.replace(original, replacement, false);
    }

    /**
     * Overloaded variant of {@link #replace(Replaceable, Replaceable)} with
     * specified placeholder state. Allows the creation of a replacement relation as
     * placeholder regardless of the state of its children replaceables.
     *
     * @param original      the replaceable to be replaced
     * @param replacement   the successor of the replaceable to be replaced
     * @param isPlaceholder indicates whether the returned relation is a placeholder
     * @return the replacement relation of this relation
     */
    public abstract <U extends Replaceable> Relation<T, S> replace(U original, U replacement, boolean isPlaceholder);

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
    public boolean isPlaceholderOf(Replaceable replaceable) {
        if (!this.isPlaceholder() || replaceable.isPlaceholder()) {
            return false;
        }
        if (getClass() != replaceable.getClass()) {
            return false;
        }
        Relation<?, ?> relation = (Relation<?, ?>) replaceable;
        return Objects.equals(destination, relation.destination) && Objects.equals(source, relation.source);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!super.equals(object)) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        Relation<?, ?> relation = (Relation<?, ?>) object;
        return Objects.equals(destination, relation.destination) && Objects.equals(source, relation.source);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(destination, source);
        return result;
    }
}
