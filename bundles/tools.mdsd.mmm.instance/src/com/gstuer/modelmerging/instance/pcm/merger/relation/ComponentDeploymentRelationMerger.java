package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentDeploymentRelation;

public class ComponentDeploymentRelationMerger extends RelationMerger<PcmSurrogate, ComponentDeploymentRelation> {
    public ComponentDeploymentRelationMerger(PcmSurrogate model) {
        super(model, ComponentDeploymentRelation.class);
    }

    @Override
    protected void refine(ComponentDeploymentRelation discovery) {
        // TODO Add missing refinement steps -> Especially replace placeholders
    }
}
