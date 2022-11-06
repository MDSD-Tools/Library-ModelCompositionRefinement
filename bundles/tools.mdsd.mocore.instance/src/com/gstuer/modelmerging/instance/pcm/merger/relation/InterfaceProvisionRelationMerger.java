package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationProcessor;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;

public class InterfaceProvisionRelationMerger extends RelationProcessor<PcmSurrogate, InterfaceProvisionRelation> {
    public InterfaceProvisionRelationMerger(PcmSurrogate model) {
        super(model, InterfaceProvisionRelation.class);
    }
}
