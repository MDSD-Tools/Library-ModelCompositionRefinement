package com.gstuer.modelmerging.framework.orchestration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.gstuer.modelmerging.framework.discovery.Discoverer;
import com.gstuer.modelmerging.framework.merger.Processor;
import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

public abstract class Orchestrator<M extends Model> {
    private final M model;
    private final Map<Class<?>, Processor<M, ?>> processorMap;

    protected Orchestrator(M model, Processor<M, ?>... mergers) {
        this.model = Objects.requireNonNull(model);
        this.processorMap = new HashMap<>();
        for (Processor<M, ?> merger : mergers) {
            processorMap.put(merger.getProcessableType(), merger);
        }
    }

    public M getModel() {
        return model;
    }

    public <T extends Replaceable> void processDiscoverer(Discoverer<T> discoverer) {
        for (T discovery : discoverer.getDiscoveries()) {
            processDiscovery(discovery);
        }
    }

    public <T extends Replaceable> void processDiscovery(T discovery) {
        // Get appropriate merger for discovery or throw exception if unavailable
        Processor<M, T> merger = getProcessorForDiscovery(discovery)
                .orElseThrow(() -> new UnavailableMergerException(discovery.getClass()));

        // Check whether discovery already exists in model to avoid infinite recursion
        if (!this.model.contains(discovery)) {
            merger.process(discovery);
            for (Replaceable implicitDiscovery : merger.getImplications()) {
                processDiscovery(implicitDiscovery);
            }
        }
    }

    protected <T extends Replaceable> Optional<Processor<M, T>> getProcessorForDiscovery(T discovery) {
        return Optional.ofNullable((Processor<M, T>) processorMap.get(discovery.getClass()));
    }
}
