package com.gstuer.modelmerging.framework.orchestration;

import com.gstuer.modelmerging.framework.creation.Merger;
import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class Orchestrator<M extends Model> {
    private final M model;
    private final Map<Class<?>, Merger<?, ?>> mergerMap;

    protected Orchestrator(M model, Merger<?, ?>... mergers) {
        this.model = Objects.requireNonNull(model);
        this.mergerMap = new HashMap<>();
        for (Merger<?, ?> merger : mergers) {
            mergerMap.put(merger.getProcessableType(), merger);
        }
    }

    protected <T extends Replaceable> Optional<Merger<M, T>> getMergerForDiscoveryType(Class<T> discoveryType) {
        return Optional.ofNullable((Merger<M, T>) mergerMap.get(discoveryType));
    }

    public M getModel() {
        return model;
    }
}
