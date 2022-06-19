package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;

public class DeploymentDeploymentRelationMerger extends RelationMerger<PcmSurrogate, DeploymentDeploymentRelation> {
    public DeploymentDeploymentRelationMerger(PcmSurrogate model) {
        super(model, DeploymentDeploymentRelation.class);
    }
}
