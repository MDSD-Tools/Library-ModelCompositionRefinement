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

public abstract class Element<T> extends Replaceable {
    private static long nextUniqueValue;
    private final T value;

    protected Element(T value, boolean isPlaceholder) {
        super(isPlaceholder);
        this.value = Objects.requireNonNull(value);
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean includes(Replaceable replaceable) {
        return this.equals(replaceable);
    }

    @Override
    public <U extends Replaceable> U replace(U original, U replacement) {
        if (!this.includes(original)) {
            throw new IllegalArgumentException();
        }
        return replacement;
    }

    @Override
    public boolean isPlaceholderOf(Replaceable replaceable) {
        if (!this.isPlaceholder() || replaceable.isPlaceholder()) {
            return false;
        }
        if (getClass() != replaceable.getClass()) {
            return false;
        }
        Element<?> element = (Element<?>) replaceable;
        return Objects.equals(value, element.value);
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
        Element<?> element = (Element<?>) object;
        return Objects.equals(value, element.value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(value);
        return result;
    }

    protected static long getUniqueValue() {
        return nextUniqueValue++;
    }
}
