/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.framework.surrogate;

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
    public boolean includes(Replaceable replaceable) {
        return this.equals(replaceable) || this.source.includes(replaceable)
                || this.destination.includes(replaceable);
    }

    @SuppressWarnings("unchecked")
    protected <U extends Replaceable> T getSourceReplacement(U original, U replacement) {
        if (this.source.includes(original)) {
            // Replace always returns an instance of the same type. See Replaceable.replace();
            return (T) this.source.replace(original, replacement);
        }
        return this.source;
    }

    @SuppressWarnings("unchecked")
    protected <U extends Replaceable> S getDestinationReplacement(U original, U replacement) {
        if (this.destination.includes(original)) {
            // Replace always returns an instance of the same type. See Replaceable.replace();
            return (S) this.destination.replace(original, replacement);
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
