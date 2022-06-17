package com.gstuer.modelmerging.framework.surrogate;

import java.util.Objects;

public abstract class Replaceable {
    private final boolean isPlaceholder;

    protected Replaceable(boolean isPlaceholder) {
        this.isPlaceholder = isPlaceholder;
    }

    public boolean isPlaceholder() {
        return isPlaceholder;
    }

    public abstract boolean canReplace(Replaceable replaceable);

    public abstract <T extends Replaceable> Replaceable replace(T original, T replacement);

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Replaceable replaceable = (Replaceable) object;
        return isPlaceholder == replaceable.isPlaceholder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPlaceholder);
    }
}
