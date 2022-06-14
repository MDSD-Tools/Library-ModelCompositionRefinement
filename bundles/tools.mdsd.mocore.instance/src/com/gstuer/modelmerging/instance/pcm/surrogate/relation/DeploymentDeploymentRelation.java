package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;

public class DeploymentDeploymentRelation extends Relation<Deployment, Deployment> {
    public DeploymentDeploymentRelation(Deployment source, Deployment destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }
}
