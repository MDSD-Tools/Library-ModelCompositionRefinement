package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationProcessor;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ServiceEffectSpecificationRelation;

public class ServiceEffectSpecificationRelationProcessor extends RelationProcessor<PcmSurrogate, ServiceEffectSpecificationRelation> {
    public ServiceEffectSpecificationRelationProcessor(PcmSurrogate model) {
        super(model, ServiceEffectSpecificationRelation.class);
    }
}
