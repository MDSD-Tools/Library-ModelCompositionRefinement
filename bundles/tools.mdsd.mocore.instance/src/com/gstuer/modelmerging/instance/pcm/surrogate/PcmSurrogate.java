package com.gstuer.modelmerging.instance.pcm.surrogate;

import java.util.List;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.framework.surrogate.TypedDistinctMultiMap;

public class PcmSurrogate implements Model {
    private final TypedDistinctMultiMap<Replaceable> replaceables;

    public PcmSurrogate() {
        this.replaceables = new TypedDistinctMultiMap<>();
    }

    @Override
    public <T extends Replaceable> List<T> getByType(Class<T> type) {
        return this.replaceables.get(type);
    }

    @Override
    public void add(Replaceable replaceable) {
        this.replaceables.put(replaceable);
    }

    @Override
    public boolean contains(Replaceable replaceable) {
        return this.replaceables.containsElement(replaceable);
    }

    @Override
    public void replace(Replaceable original, Replaceable replacement) {
        for (Replaceable predecessor : this.replaceables) {
            if (predecessor.canReplace(original)) {
                Replaceable successor = predecessor.replace(original, replacement);
                this.replaceables.remove(predecessor);
                this.add(successor);
            }
        }
    }
}
