package com.gstuer.modelmerging.test.utility;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

public class SimpleRelation extends Relation<SimpleElement, SimpleElement> {
    public SimpleRelation(SimpleElement source, SimpleElement destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }

    @Override
    public <T extends Replaceable> SimpleRelation replace(T original, T replacement) {
        if (!this.canReplace(original)) {
            throw new IllegalArgumentException("Replace may not be called with the given original.");
        }
        if (this.equals(original)) {
            return (SimpleRelation) replacement;
        }
        SimpleElement source = getSourceReplacement(original, replacement);
        SimpleElement destination = getDestinationReplacement(original, replacement);
        return new SimpleRelation(source, destination, this.isPlaceholder());
    }
}
