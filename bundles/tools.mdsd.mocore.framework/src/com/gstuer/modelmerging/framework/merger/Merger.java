package com.gstuer.modelmerging.framework.merger;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

public abstract class Merger<M extends Model, T extends Replaceable> {
    private final M model;
    private final Set<Replaceable> implications;
    private final Class<T> processableType;

    protected Merger(M model, Class<T> processableType) {
        this.model = Objects.requireNonNull(model);
        this.implications = new HashSet<>();
        this.processableType = Objects.requireNonNull(processableType);
    }

    public void process(T discovery) {
        this.implications.clear();
        merge(discovery);
        refine(discovery);
    }

    protected void merge(T discovery) {
        this.model.add(discovery);
    }

    protected abstract void refine(T discovery);

    public M getModel() {
        return model;
    }

    public Set<Replaceable> getImplications() {
        return Set.copyOf(implications);
    }

    protected void addImplication(Replaceable replaceable) {
        this.implications.add(replaceable);
    }

    public Class<T> getProcessableType() {
        return processableType;
    }
}
