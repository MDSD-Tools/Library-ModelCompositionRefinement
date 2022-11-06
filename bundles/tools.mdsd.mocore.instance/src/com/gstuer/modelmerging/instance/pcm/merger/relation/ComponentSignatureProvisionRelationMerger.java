package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationProcessor;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentSignatureProvisionRelation;

public class ComponentSignatureProvisionRelationMerger
        extends RelationProcessor<PcmSurrogate, ComponentSignatureProvisionRelation> {
    public ComponentSignatureProvisionRelationMerger(PcmSurrogate model) {
        super(model, ComponentSignatureProvisionRelation.class);
    }
}
