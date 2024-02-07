/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.framework.surrogate;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Model {
    private final TypedDistinctMultiMap<Replaceable> replaceables;

    public Model() {
        this.replaceables = new TypedDistinctMultiMap<>();
    }

    public <T extends Replaceable> List<T> getByType(Class<T> type) {
        return this.replaceables.get(type);
    }

    public void add(Replaceable replaceable) {
        this.replaceables.put(replaceable);
    }

    public boolean contains(Replaceable replaceable) {
        return this.replaceables.containsElement(replaceable);
    }

    /**
     * Executes a replacement of a certain {@link Replaceable replaceable} in the model. For this purpose, all affected
     * replaceables are removed from the model. The appropriate successors of the removed replaceables are created and
     * returned but not added to the model. The given original replaceable to be replaced is also removed from the
     * model. The given replacement is part of the returned successors but not added to the model.
     *
     * @param original    the replaceable to be replaced in the model
     * @param replacement the successor of the replaceable to be replaced
     * @return The successors of the affected and removed replaceables in the model including the original's
     *         replacement.
     */
    public Set<Replaceable> replace(Replaceable original, Replaceable replacement) {
        if (Objects.isNull(original) || Objects.isNull(replacement)) {
            throw new NullPointerException();
        }
        Set<Replaceable> implications = new HashSet<>();
        for (Replaceable predecessor : this.replaceables) {
            if (predecessor.includes(original)) {
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
