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
        // Add trivial implications for source and destination elements
        addImplication(discovery.getSource());
        addImplication(discovery.getDestination());

        // TODO Add missing refinement steps -> Especially replace placeholders
    }
}
