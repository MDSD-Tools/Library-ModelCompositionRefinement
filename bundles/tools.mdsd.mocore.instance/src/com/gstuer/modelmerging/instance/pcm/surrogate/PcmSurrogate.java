package com.gstuer.modelmerging.instance.pcm.surrogate;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.framework.surrogate.TypedDistinctMultiMap;

import java.util.List;

public class PcmSurrogate implements Model {
    private final TypedDistinctMultiMap replaceables;

    public PcmSurrogate() {
        this.replaceables = new TypedDistinctMultiMap();
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
}
