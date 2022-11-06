package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationProcessor;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;

public class InterfaceRequirementRelationProcessor extends RelationProcessor<PcmSurrogate, InterfaceRequirementRelation> {
    public InterfaceRequirementRelationProcessor(PcmSurrogate model) {
        super(model, InterfaceRequirementRelation.class);
    }
}
