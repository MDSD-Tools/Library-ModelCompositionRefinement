package com.gstuer.modelmerging.framework.surrogate;

public abstract class Replaceable {
    private final boolean isPlaceholder;

    protected Replaceable(boolean isPlaceholder) {
        this.isPlaceholder = isPlaceholder;
    }

    public boolean isPlaceholder() {
        return isPlaceholder;
    }
    
    public abstract boolean canReplace(Replaceable replaceable);
    
    public <T extends Replaceable> Replaceable replace(T original, T replacement) {
        throw new UnsupportedOperationException();
    }
}
