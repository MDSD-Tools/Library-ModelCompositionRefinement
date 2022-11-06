package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationProcessor;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;

public class SignatureProvisionRelationProcessor extends RelationProcessor<PcmSurrogate, SignatureProvisionRelation> {
    public SignatureProvisionRelationProcessor(PcmSurrogate model) {
        super(model, SignatureProvisionRelation.class);
    }
}
