package com.gstuer.modelmerging.framework.discovery;

import com.gstuer.modelmerging.framework.surrogate.Replaceable;

import java.util.Objects;
import java.util.Set;

public abstract class Discoverer<T extends Replaceable> {
    private final Set<T> discoveries;
    private final Class<T> discoveryType;

    protected Discoverer(Set<T> discoveries, Class<T> discoveryType) {
        this.discoveries = Set.copyOf(discoveries);
        this.discoveryType = Objects.requireNonNull(discoveryType);
    }

    public Set<T> getDiscoveries() {
        return discoveries;
    }

    public Class<T> getDiscoveryType() {
        return discoveryType;
    }
}
