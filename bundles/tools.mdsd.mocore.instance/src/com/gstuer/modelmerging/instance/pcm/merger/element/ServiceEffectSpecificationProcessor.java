package com.gstuer.modelmerging.instance.pcm.merger.element;

import com.gstuer.modelmerging.framework.merger.Processor;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.ServiceEffectSpecification;

public class ServiceEffectSpecificationProcessor extends Processor<PcmSurrogate, ServiceEffectSpecification> {
    public ServiceEffectSpecificationProcessor(PcmSurrogate model) {
        super(model, ServiceEffectSpecification.class);
    }

    @Override
    protected void refine(ServiceEffectSpecification discovery) {
        // TODO Evaluate whether refinement should be done for a single specification element
    }
}
