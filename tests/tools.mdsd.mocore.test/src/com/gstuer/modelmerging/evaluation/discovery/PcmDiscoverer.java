package com.gstuer.modelmerging.evaluation.discovery;

import java.util.Set;

import com.gstuer.modelmerging.framework.discovery.Discoverer;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

public class PcmDiscoverer<T extends Replaceable> extends Discoverer<T> {
    public PcmDiscoverer(Set<T> discoveries, Class<T> discoveryType) {
        super(discoveries, discoveryType);
    }
}
