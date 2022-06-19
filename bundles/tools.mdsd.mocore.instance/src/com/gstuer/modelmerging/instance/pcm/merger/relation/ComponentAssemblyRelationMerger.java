package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAssemblyRelation;

public class ComponentAssemblyRelationMerger extends RelationMerger<PcmSurrogate, ComponentAssemblyRelation> {
    public ComponentAssemblyRelationMerger(PcmSurrogate model) {
        super(model, ComponentAssemblyRelation.class);
    }
}
