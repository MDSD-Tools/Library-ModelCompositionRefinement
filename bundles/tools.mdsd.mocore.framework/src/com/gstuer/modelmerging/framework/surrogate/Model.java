package com.gstuer.modelmerging.framework.surrogate;

import java.util.List;
import java.util.Set;

public interface Model {
    <T extends Replaceable> List<T> getByType(Class<T> type);

    void add(Replaceable replaceable);

    boolean contains(Replaceable replaceable);

    /**
     * Executes a replacement of a certain {@link Replaceable replaceable} in the
     * model. For this purpose, all affected replaceables are removed from the
     * model. The appropriate successors of the removed replaceables are created and
     * returned but not added to the model. The given original replaceable to be
     * replaced is also removed from the model. The given replacement is part of the
     * returned successors but not added to the model.
     *
     * @param original    the replaceable to be replaced in the model
     * @param replacement the successor of the replaceable to be replaced
     * @return The successors of the affected and removed replaceables in the model
     *         including the original's replacement.
     */
    Set<Replaceable> replace(Replaceable original, Replaceable replacement);
}
