package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureSpecificationRelation;

public class SignatureSpecificationRelationMerger extends RelationMerger<PcmSurrogate, SignatureSpecificationRelation> {
    public SignatureSpecificationRelationMerger(PcmSurrogate model) {
        super(model, SignatureSpecificationRelation.class);
    }
}
