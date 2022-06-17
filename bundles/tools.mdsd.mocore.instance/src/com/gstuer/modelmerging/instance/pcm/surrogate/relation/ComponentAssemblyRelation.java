package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;

// TODO Migrate to Component Component Interface or Provides Requires Relation
public class ComponentAssemblyRelation extends Relation<Component, Component> {
    public ComponentAssemblyRelation(Component source, Component destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }

    @Override
    public <U extends Replaceable> ComponentAssemblyRelation replace(U original, U replacement) {
        if (!this.canReplace(original)) {
            // TODO Add message to exception
            throw new IllegalArgumentException();
        }
        if (this.equals(original)) {
            return (ComponentAssemblyRelation) replacement;
        }
        Component source = getSourceReplacement(original, replacement);
        Component destination = getDestinationReplacement(original, replacement);
        return new ComponentAssemblyRelation(source, destination, this.isPlaceholder());
    }
}
