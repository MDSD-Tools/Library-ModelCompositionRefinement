package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;

public class ComponentDeploymentRelation extends Relation<Component, Deployment> {
    public ComponentDeploymentRelation(Component source, Deployment destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }
}
