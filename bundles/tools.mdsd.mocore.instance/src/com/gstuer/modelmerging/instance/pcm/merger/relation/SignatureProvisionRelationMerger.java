package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;

public class SignatureProvisionRelationMerger extends RelationMerger<PcmSurrogate, SignatureProvisionRelation> {
    public SignatureProvisionRelationMerger(PcmSurrogate model) {
        super(model, SignatureProvisionRelation.class);
    }
}
