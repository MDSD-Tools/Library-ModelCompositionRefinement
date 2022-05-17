package com.gstuer.modelmerging.framework.creation;

import com.gstuer.modelmerging.framework.discovery.Discoverer;

import java.util.Objects;
import java.util.Optional;

public abstract class Creator<M extends Model> {
    private static final String UNSUPPORTED_DISCOVERER = "No merger available for discoverer of type %s.";

    private M model;

    protected Creator(M model) {
        this.model = Objects.requireNonNull(model);
    }

    public M create(Discoverer<?>... discoverers) {
        for (Discoverer<?> discoverer : discoverers) {
            mergeDiscoverer(discoverer);
        }
        return this.model;
    }

    protected abstract <T> Optional<Merger<M, T>> getMergerForDiscoveryType(Class<T> discoveryType);

    private <K> void mergeDiscoverer(Discoverer<K> discoverer) {
        Optional<Merger<M, K>> optionalMerger = getMergerForDiscoveryType(discoverer.getDiscoveryType());
        if (optionalMerger.isPresent()) {
            Merger<M, K> merger = optionalMerger.get();
            for (K discovery : discoverer.getDiscoveries()) {
                this.model = merger.merge(this.model, discovery);
            }
        } else {
            throw new UnsupportedDiscovererException(String.format(UNSUPPORTED_DISCOVERER, discoverer.getClass()));
        }
    }
}
