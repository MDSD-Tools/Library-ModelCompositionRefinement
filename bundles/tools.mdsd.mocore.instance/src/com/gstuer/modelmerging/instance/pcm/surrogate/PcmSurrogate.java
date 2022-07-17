package com.gstuer.modelmerging.instance.pcm.surrogate;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    public Set<Replaceable> replace(Replaceable original, Replaceable replacement) {
        if (Objects.isNull(original) || Objects.isNull(replacement)) {
            throw new NullPointerException();
        }
        Set<Replaceable> implications = new HashSet<>();
        for (Replaceable predecessor : this.replaceables) {
            if (predecessor.canReplace(original)) {
                /*
                 * If a predecessor needs replacement, create successor of obsolete replaceable. Then delete obsolete
                 * replaceable from model and return successor as implication for further merging. The model cannot add
                 * the successor itself because it might have consequences for other replaceables in the model.
                 */
                Replaceable successor = predecessor.replace(original, replacement);
                this.replaceables.remove(predecessor);
                implications.add(successor);
            }
        }
        return implications;
    }
}
