package com.gstuer.modelmerging.instance.pcm.merger.relation.pseudo;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.pseudo.ComponentSignatureProvisionRelation;

public class ComponentSignatureProvisionRelationMerger
        extends RelationMerger<PcmSurrogate, ComponentSignatureProvisionRelation> {
    public ComponentSignatureProvisionRelationMerger(PcmSurrogate model) {
        super(model, ComponentSignatureProvisionRelation.class);
    }
}
