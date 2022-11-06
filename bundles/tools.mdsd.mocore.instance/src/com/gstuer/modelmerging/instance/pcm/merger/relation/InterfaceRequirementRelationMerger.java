package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationProcessor;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;

public class InterfaceRequirementRelationMerger extends RelationProcessor<PcmSurrogate, InterfaceRequirementRelation> {
    public InterfaceRequirementRelationMerger(PcmSurrogate model) {
        super(model, InterfaceRequirementRelation.class);
    }
}
