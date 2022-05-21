package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentDeploymentRelation;

public class ComponentDeploymentRelationMerger extends Merger<PcmSurrogate, ComponentDeploymentRelation> {
    public ComponentDeploymentRelationMerger(PcmSurrogate model) {
        super(model, ComponentDeploymentRelation.class);
    }

    @Override
    protected void refine(ComponentDeploymentRelation discovery) {
        // Add trivial implications for source and destination elements
        addImplication(discovery.getSource());
        addImplication(discovery.getDestination());

        // TODO Add missing refinement steps -> Especially replace placeholders
    }
}
