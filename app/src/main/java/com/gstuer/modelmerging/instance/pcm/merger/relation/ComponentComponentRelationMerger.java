package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentComponentRelation;

public class ComponentComponentRelationMerger extends Merger<PcmSurrogate, ComponentComponentRelation> {
    public ComponentComponentRelationMerger(PcmSurrogate model) {
        super(model, ComponentComponentRelation.class);
    }

    @Override
    protected void refine(ComponentComponentRelation discovery) {
        // TODO Add refinement steps
    }
}
