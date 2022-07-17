package com.gstuer.modelmerging.instance.pcm.surrogate;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.PseudoReplaceableCreator;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.framework.surrogate.TypedDistinctMultiMap;

public class PcmSurrogate implements Model {
    private final TypedDistinctMultiMap<Replaceable> replaceables;
    private final Set<PseudoReplaceableCreator<?>> pseudoCreators;

    public PcmSurrogate() {
        this.replaceables = new TypedDistinctMultiMap<>();
        this.pseudoCreators = new HashSet<>();
    }

    @Override
    public <T extends Replaceable> List<T> getByType(Class<T> type) {
        if (existsPseudoCreator(type)) {
            PseudoReplaceableCreator<T> pseudoCreator = getPseudoCreator(type).orElseThrow();
            return pseudoCreator.getPseudoReplaceables(this);
        }
        return this.replaceables.get(type);
    }

    @Override
    public void add(Replaceable replaceable) {
        if (!existsPseudoCreator(replaceable.getClass())) {
            this.replaceables.put(replaceable);
        }
    }

    @Override
    public boolean contains(Replaceable replaceable) {
        List<?> pseudoReplaceables = new LinkedList<>();
        if (replaceable != null && existsPseudoCreator(replaceable.getClass())) {
            PseudoReplaceableCreator<?> pseudoCreator = getPseudoCreator(replaceable.getClass()).orElseThrow();
            pseudoReplaceables = pseudoCreator.getPseudoReplaceables(this);
        }
        return pseudoReplaceables.contains(replaceable) || this.replaceables.containsElement(replaceable);
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

    private <T extends Replaceable> boolean existsPseudoCreator(Class<T> type) {
        return getPseudoCreator(type).isPresent();
    }

    @SuppressWarnings("unchecked")
    private <T extends Replaceable> Optional<PseudoReplaceableCreator<T>> getPseudoCreator(
            Class<T> type) {
        for (PseudoReplaceableCreator<?> creator : this.pseudoCreators) {
            if (creator.getReplaceableType().equals(type)) {
                // Type-safe cast due to type check in if-clause
                return Optional.of((PseudoReplaceableCreator<T>) creator);
            }
        }
        return Optional.empty();
    }
}
