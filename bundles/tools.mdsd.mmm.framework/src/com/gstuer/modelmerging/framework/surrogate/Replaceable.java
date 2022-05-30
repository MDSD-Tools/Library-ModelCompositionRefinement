package com.gstuer.modelmerging.framework.surrogate;

public abstract class Replaceable {
    private final boolean isPlaceholder;

    protected Replaceable(boolean isPlaceholder) {
        this.isPlaceholder = isPlaceholder;
    }

    public boolean isPlaceholder() {
        return isPlaceholder;
    }
}
