package com.gstuer.modelmerging.framework.creation;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class Merger<M extends Model, T extends Replaceable> {
    private final M model;
    private final Set<Replaceable> implications;

    protected Merger(M model) {
        this.model = Objects.requireNonNull(model);
        this.implications = new HashSet<>();
    }

    public abstract void merge(T discovery);

    public M getModel() {
        return model;
    }

    public Set<Replaceable> getImplications() {
        return Set.copyOf(implications);
    }
}
