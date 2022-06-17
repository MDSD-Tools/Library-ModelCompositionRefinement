package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;

// TODO Migrate to Component Component Interface or Provides Requires Relation
public class ComponentComponentRelation extends Relation<Component, Component> {
    public ComponentComponentRelation(Component source, Component destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }

    @Override
    public <U extends Replaceable> ComponentComponentRelation replace(U original, U replacement) {
        if (!this.canReplace(original)) {
            // TODO Add message to exception
            throw new IllegalArgumentException();
        }
        if (this.equals(original)) {
            return (ComponentComponentRelation) replacement;
        }
        Component source = getSourceReplacement(original, replacement);
        Component destination = getDestinationReplacement(original, replacement);
        return new ComponentComponentRelation(source, destination, this.isPlaceholder());
    }
}
