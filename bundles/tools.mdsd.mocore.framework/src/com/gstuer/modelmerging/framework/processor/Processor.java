package com.gstuer.modelmerging.framework.processor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

public abstract class Processor<M extends Model, T extends Replaceable> {
    private final M model;
    private final Set<Replaceable> implications;
    private final Class<T> processableType;

    protected Processor(M model, Class<T> processableType) {
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

    protected void addImplications(Iterable<Replaceable> replaceables) {
        for (Replaceable replaceable : replaceables) {
            this.implications.add(replaceable);
        }
    }

    protected void removeImplication(Replaceable replaceable) {
        this.implications.remove(replaceable);
    }

    protected void removeImplications(Iterable<Replaceable> replaceables) {
        for (Replaceable replaceable : replaceables) {
            this.implications.remove(replaceable);
        }
    }

    protected void replaceImplications(Replaceable original, Replaceable replacement) {
        Set<Replaceable> predecessors = new HashSet<>();
        Set<Replaceable> successors = new HashSet<>();
        for (Replaceable implication : this.implications) {
            if (implication.canReplace(original)) {
                Replaceable successor = implication.replace(original, replacement);
                predecessors.add(implication);
                successors.add(successor);
            }
        }
        this.implications.removeAll(predecessors);
        this.implications.addAll(successors);
    }

    public Class<T> getProcessableType() {
        return processableType;
    }
}
