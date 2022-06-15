package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;

public class DeploymentDeploymentRelation extends Relation<Deployment, Deployment> {
    public DeploymentDeploymentRelation(Deployment source, Deployment destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }

    @Override
    public <U extends Replaceable> DeploymentDeploymentRelation replace(U original, U replacement,
            boolean isPlaceholder) {
        if (!this.canReplace(original)) {
            // TODO Add message to exception
            throw new IllegalArgumentException();
        }
        Deployment source = getSourceReplacement(original, replacement);
        Deployment destination = getDestinationReplacement(original, replacement);
        return new DeploymentDeploymentRelation(source, destination, isPlaceholder);
    }
}
