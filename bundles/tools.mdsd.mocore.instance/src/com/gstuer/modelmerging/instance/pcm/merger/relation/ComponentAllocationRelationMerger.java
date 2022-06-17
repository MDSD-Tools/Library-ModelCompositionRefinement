package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;

public class ComponentAllocationRelationMerger extends RelationMerger<PcmSurrogate, ComponentAllocationRelation> {
    public ComponentAllocationRelationMerger(PcmSurrogate model) {
        super(model, ComponentAllocationRelation.class);
    }

    @Override
    protected void refine(ComponentAllocationRelation discovery) {
        // TODO Add missing refinement steps -> Especially replace placeholders
    }
}
