package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;

public class ComponentDeploymentRelation extends Relation<Component, Deployment> {
    public ComponentDeploymentRelation(Component source, Deployment destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }

    @Override
    public <U extends Replaceable> ComponentDeploymentRelation replace(U original, U replacement) {
        if (!this.canReplace(original)) {
            // TODO Add message to exception
            throw new IllegalArgumentException();
        }
        if (this.equals(original)) {
            return (ComponentDeploymentRelation) replacement;
        }
        Component source = getSourceReplacement(original, replacement);
        Deployment destination = getDestinationReplacement(original, replacement);
        return new ComponentDeploymentRelation(source, destination, this.isPlaceholder());
    }
}
