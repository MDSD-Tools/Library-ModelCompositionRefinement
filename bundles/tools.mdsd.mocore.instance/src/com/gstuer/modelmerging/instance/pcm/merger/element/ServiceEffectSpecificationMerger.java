package com.gstuer.modelmerging.instance.pcm.merger.element;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.ServiceEffectSpecification;

public class ServiceEffectSpecificationMerger extends Merger<PcmSurrogate, ServiceEffectSpecification> {
    public ServiceEffectSpecificationMerger(PcmSurrogate model) {
        super(model, ServiceEffectSpecification.class);
    }

    @Override
    protected void refine(ServiceEffectSpecification discovery) {
        // TODO Evaluate whether refinement should be done for a single specification element
    }
}
