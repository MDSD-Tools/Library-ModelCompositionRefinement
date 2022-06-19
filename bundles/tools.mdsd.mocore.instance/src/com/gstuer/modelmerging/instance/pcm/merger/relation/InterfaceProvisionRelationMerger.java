package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;

public class InterfaceProvisionRelationMerger extends RelationMerger<PcmSurrogate, InterfaceProvisionRelation> {
    public InterfaceProvisionRelationMerger(PcmSurrogate model) {
        super(model, InterfaceProvisionRelation.class);
    }
}
