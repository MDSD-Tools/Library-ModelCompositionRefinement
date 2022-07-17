package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ServiceEffectSpecificationRelation;

public class ServiceEffectSpecificationRelationMerger extends RelationMerger<PcmSurrogate, ServiceEffectSpecificationRelation> {
    public ServiceEffectSpecificationRelationMerger(PcmSurrogate model) {
        super(model, ServiceEffectSpecificationRelation.class);
    }
}
