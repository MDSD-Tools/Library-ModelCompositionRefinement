package com.gstuer.modelmerging.framework.surrogate;

import java.util.List;
import java.util.Objects;

public abstract class PseudoReplaceableCreator<T extends Replaceable> {
    private final Class<T> replaceableType;

    protected PseudoReplaceableCreator(Class<T> replaceableType) {
        this.replaceableType = Objects.requireNonNull(replaceableType);
    }

    public abstract List<T> getPseudoReplaceables(Model model);

    public Class<T> getReplaceableType() {
        return this.replaceableType;
    }
}
