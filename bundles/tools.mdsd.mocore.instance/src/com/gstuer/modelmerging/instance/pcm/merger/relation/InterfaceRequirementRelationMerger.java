package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;

public class InterfaceRequirementRelationMerger extends RelationMerger<PcmSurrogate, InterfaceRequirementRelation> {
    public InterfaceRequirementRelationMerger(PcmSurrogate model) {
        super(model, InterfaceRequirementRelation.class);
    }
}
