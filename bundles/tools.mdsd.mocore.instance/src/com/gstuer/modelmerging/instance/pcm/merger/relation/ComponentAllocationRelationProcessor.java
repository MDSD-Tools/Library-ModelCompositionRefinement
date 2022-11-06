package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationProcessor;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;

public class ComponentAllocationRelationProcessor extends RelationProcessor<PcmSurrogate, ComponentAllocationRelation> {
    public ComponentAllocationRelationProcessor(PcmSurrogate model) {
        super(model, ComponentAllocationRelation.class);
    }
}
